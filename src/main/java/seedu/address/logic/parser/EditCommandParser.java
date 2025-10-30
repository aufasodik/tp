package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditCompanyDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.parser.exceptions.ParseIndicesException;
import seedu.address.model.company.Address;
import seedu.address.model.company.Email;
import seedu.address.model.company.Phone;
import seedu.address.model.company.Remark;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {

    private static final Logger logger = LogsCenter.getLogger(EditCommandParser.class);

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * Supports both single index editing (e.g., "1") and batch editing (e.g., "1,2,3").
     *
     * @param args the user input arguments
     * @return EditCommand configured for single or batch editing
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        logger.fine("Parsing edit command with args: " + args);
        if (args.trim().isEmpty()) {
            logger.warning("Empty arguments provided to edit command");
            throw new ParseException(String.format(EditCommand.MESSAGE_MISSING_INDEX));
        }
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE,
                PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TAG, PREFIX_REMARK, PREFIX_STATUS);

        List<Index> indices = validatePreamble(argMultimap);

        EditCompanyDescriptor editCompanyDescriptor = new EditCompanyDescriptor();
        parseEditFields(argMultimap, editCompanyDescriptor);

        logger.fine(String.format("Successfully parsed edit command for %d indices", indices.size()));
        return new EditCommand(indices, editCompanyDescriptor);
    }

    /**
     * Validates the preamble and parses the indices.
     * @param argMultimap the parsed argument multimap
     * @return list of parsed indices
     * @throws ParseException if validation fails or indices cannot be parsed
     */
    private List<Index> validatePreamble(ArgumentMultimap argMultimap) throws ParseException {
        if (argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(EditCommand.MESSAGE_MISSING_INDEX);
        }

        if (argMultimap.getPreamble().contains("/")) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }

        try {
            return ParserUtil.parseIndices(argMultimap.getPreamble());
        } catch (ParseIndicesException pie) {
            throw pie;
        }
    }

    /**
     * Parses edit fields from the argument multimap into the edit descriptor.
     * @param argMultimap the parsed argument multimap
     * @param editCompanyDescriptor the descriptor to populate
     * @throws ParseException if parsing fails
     */
    private void parseEditFields(ArgumentMultimap argMultimap, EditCompanyDescriptor editCompanyDescriptor)
            throws ParseException {
        assert argMultimap != null && editCompanyDescriptor != null
                : "Arguments to parseEditFields should not be null";
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS,
                PREFIX_REMARK, PREFIX_STATUS);

        parseFieldIfPresent(argMultimap, PREFIX_NAME, ParserUtil::parseName, editCompanyDescriptor::setName);
        parseNullableFieldIfPresent(argMultimap, PREFIX_PHONE, ParserUtil::parsePhone,
                Phone::new, editCompanyDescriptor::setPhone);
        parseNullableFieldIfPresent(argMultimap, PREFIX_EMAIL, ParserUtil::parseEmail,
                Email::new, editCompanyDescriptor::setEmail);
        parseNullableFieldIfPresent(argMultimap, PREFIX_ADDRESS, ParserUtil::parseAddress,
                Address::new, editCompanyDescriptor::setAddress);
        parseNullableFieldIfPresent(argMultimap, PREFIX_REMARK, ParserUtil::parseRemark,
                Remark::new, editCompanyDescriptor::setRemark);
        parseFieldIfPresent(argMultimap, PREFIX_STATUS, ParserUtil::parseStatus, editCompanyDescriptor::setStatus);
        parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editCompanyDescriptor::setTags);

        if (!editCompanyDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }
    }

    /**
     * Functional interface for parsing operations that may throw ParseException.
     */
    @FunctionalInterface
    private interface ParserFunction<T> {
        T parse(String input) throws ParseException;
    }

    /**
     * Functional interface for creating objects with null input.
     */
    @FunctionalInterface
    private interface NullConstructor<T> {
        T create(String input);
    }

    /**
     * Parses a field if present and applies the setter.
     */
    private <T> void parseFieldIfPresent(ArgumentMultimap argMultimap, Prefix prefix,
            ParserFunction<T> parser, Consumer<T> setter) throws ParseException {
        Optional<String> value = argMultimap.getValue(prefix);
        if (value.isPresent()) {
            T parsed = parser.parse(value.get());
            setter.accept(parsed);
        }
    }

    /**
     * Parses a nullable field if present, handling blank values by creating null objects.
     * Preserves exact behavior: val.isBlank() ? new Type(null) : ParserUtil.parseType(val)
     */
    private <T> void parseNullableFieldIfPresent(ArgumentMultimap argMultimap, Prefix prefix,
            ParserFunction<T> parser, NullConstructor<T> nullConstructor, Consumer<T> setter) throws ParseException {
        Optional<String> value = argMultimap.getValue(prefix);
        if (value.isPresent()) {
            String val = value.get();
            T parsed = val.isBlank() ? nullConstructor.create(null) : parser.parse(val);
            setter.accept(parsed);
        }
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

}
