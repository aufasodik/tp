# Cerebro v1.0.1 - Patch Release

## Release Date
October 2025

---

## Overview

This is a minor patch release addressing a known issue from v1.0. No new features or functional changes have been introduced.

---

## Bug Fixes

### **Help Window Minimization Issue** ✅ RESOLVED

**Issue:** Minimized help window did not reappear when `help` command was called again.

**Fix:** Help window now properly restores and comes to focus when the `help` command is invoked, regardless of whether it was previously minimized.

**Impact:** Users can now reliably access help documentation without needing to manually restore minimized windows.

---

## What's Unchanged

All existing features from v1.0 remain fully functional:
- Application status tracking system
- Flexible company entry system
- Remark system
- Status filtering
- Batch operations (edit/delete)
- All core commands (add, edit, delete, list, find, etc.)

---

## Known Issues

The following known issue remains:

1. **Multi-screen GUI positioning** - GUI may open off-screen after switching from secondary to primary screen
    - **Remedy:** Delete `preferences.json` before restarting

---

## Upgrade Notes

**For v1.0 Users:**
- No data migration required
- All existing data files remain compatible
- Simply replace your JAR file with the new version

---

*Cerebro v1.0.1 - Improved User Experience*
