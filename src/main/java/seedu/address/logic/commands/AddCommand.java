package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.company.Company;

/**
 * Adds a company to the address book.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = String.format("""
    %1$s %2$sNAME [%3$sPHONE] [%4$sEMAIL] [%5$sADDRESS] [%6$sREMARK] [%7$sSTATUS] [%8$sTAG]...
    ---------
    Add a company to Cerebro.
    ---------
    Examples:
    %1$s %2$sGoogle Inc
    %1$s %2$sMeta %3$s65432100 %4$scareers@meta.com %7$sapplied
    %1$s %2$sGoogle %3$s67676767 %4$sgoogle@example.com %5$s311, Clementi Ave 2, \
    #02-25 %6$sFAANG\
    Jackpot %7$sin-process %8$sgood-pay %8$sgood-location
        """,
            COMMAND_WORD,
            PREFIX_NAME,
            PREFIX_PHONE,
            PREFIX_EMAIL,
            PREFIX_ADDRESS,
            PREFIX_REMARK,
            PREFIX_STATUS,
            PREFIX_TAG);

    public static final String MESSAGE_SUCCESS = "New company added: %1$s";
    public static final String MESSAGE_DUPLICATE_COMPANY = "This company already exists in the address book";

    private final Company toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Company}
     */
    public AddCommand(Company company) {
        requireNonNull(company);
        toAdd = company;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasCompany(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_COMPANY);
        }

        model.addCompany(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddCommand)) {
            return false;
        }

        AddCommand otherAddCommand = (AddCommand) other;
        return toAdd.equals(otherAddCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
