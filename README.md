# RIT Shifts Tracker

A command line application for keeping track of my shifts worked in RIT Dining.

## [Shifts](./src/main/java/tracker/Shift.java)

**Definition: A period of time worked.**

Each `Shift` keeps track of:

- location worked
- date worked
- time clocked in
- time clocked out
- pay rate

The location worked is set using a static Array of locations worked at, to avoid repeatedly typing in the same location names.
The pay rate also has a default value that can be used instead of prompting for user input.

## [PayPeriod](./src/main/java/tracker/PayPeriod.java)

**Definition: A two-week (13 day) period that counts towards the next pay check.**

Each `PayPeriod` keeps track of:

- start date
- end date
- total hours
- total earned
- shifts worked

The total hours worked and total earned are incremented with every `Shift` added to the SortedSet of `Shifts` in the `PayPeriod`.

The total earned calculated is the gross pay, not accounting for any amount withheld.

## Main Application

```
Usage: RIT Dining Shift Tracker [-hV] [COMMAND]
A command line program for keeping track of my shifts worked in RIT Dining with JSON files.
  -h, --help      Show this help message and exit.
  -V, --version   Print version information and exit.
Commands:
  add     Add a Shift to a PayPeriod JSON file.
  new     Create a new PayPeriod JSON file.
  edit    Edit a Shift in a PayPeriod JSON file.
  read    Read a PayPeriod from a JSON file.
  remove  Remove a Shift from a PayPeriod JSON file.
```

[The main application](./src/main/java/tracker/App.java) was developed to work specifically
with the [Picocli library](https://picocli.info/) to handle command line arguments.
The application takes user input to create `Shifts` and `PayPeriods`, then writes a `PayPeriod` object to a file as a JSON object,
using the [Jackson library](https://github.com/FasterXML/jackson).

### New Subcommand

```
Usage: RIT Dining Shift Tracker new <directory>
Create a new PayPeriod JSON file.
      <directory>   Directory to create a new PayPeriod JSON file.
```

The **new** subcommand allows a user to create a new `PayPeriod`, prompting only for the start date of the `PayPeriod`.

The _mandatory_ `directory` argument is used to save the `PayPeriod` as a JSON object in a JSON file in the specified directory.
The file is saved based on the starting date of the `PayPeriod` in the format: `YYYY-MM-DD.json`.
If there is already a file with that name in the given directory (ie a `PayPeriod` with the same start date), the new `PayPeriod` is not saved.

### Read Subcommand

```
Usage: RIT Dining Shift Tracker read <file-path>
Read a PayPeriod from a JSON file.
      <file-path>   Path to a PayPeriod JSON file.
```

The **read** subcommand prints out information about a `PayPeriod` to the user.

The _mandatory_ `file-path` argument is used to create a `PayPeriod` object from the given JSON file.
Information about the `PayPeriod` including its `Shifts` are then printed out the command line.

### Add Subcommand

```
Usage: RIT Dining Shift Tracker add [-d] [-n=<number>] <file-path>
Add a Shift to a PayPeriod JSON file.
      <file-path>          Path to a PayPeriod JSON file.
  -d, --default-pay-rate   Create new Shift with default pay rate of $14.2.
                             Applies to all shifts being creating when this command is run.
  -n, --number=<integer>   Number of Shifts to create in this PayPeriod JSON file.
```

The **add** subcommand allows a user to create a new `Shift`,
prompting the user for information (location, date, time clocked in, time clocked out, and pay rate)
for a new `Shift` to create, which is then added to a `PayPeriod`, and updates the total hours and earned fields in the `PayPeriod`.

The _mandatory_ `file-path` argument is used to create a `PayPeriod` object from the given JSON file.
The `PayPeriod` is appended with a new `Shift`, and then saved in the file it was parsed from.

The _optional_ `--default-pay-rate` flag is used to skip user prompting for the pay rate to set for any new `Shifts` created,
using the default value. If the flag is not set, the application will prompt for the pay rate for each `Shift` being created.

The _optional_ `--number=<integer>` flag is used to add multiple `Shifts` to a `PayPeriod`.
The user will be prompted to enter information about each `Shift` to create and add to a `PayPeriod`.

### Edit Subcommand

```
Usage: RIT Dining Shift Tracker edit <file-path>
Edit a Shift in a PayPeriod JSON file.
      <file-path>   Path to a PayPeriod JSON file.
```

The **edit** subcommand allows the user to select an existing `Shift` (from a `PayPeriod`) to edit,
and updates the total hours and earned fields in the `PayPeriod` as needed.

The _mandatory_ `file-path` argument is used to create a `PayPeriod` object from the given JSON file.
The user selects a `Shift` from the `Shifts` in this `PayPeriod` to edit.
The `PayPeriod` is updated with the updated `Shift`, and then saved in the file it was parsed from.

### Remove Subcommand

```
Usage: RIT Dining Shift Tracker remove <file-path>
Remove a Shift from a PayPeriod JSON file.
      <file-path>   Path to a PayPeriod JSON file.
```

The **remove** subcommand allows the user to select an existing `Shift` (from a `PayPeriod`) to remove,
and updates the total hours and earned fields in the `PayPeriod`.

The _mandatory_ `file-path` argument is used to create a `PayPeriod` object from the given JSON file.
The user selects a `Shift` from the `Shifts` in this `PayPeriod` to remove.
The selected `Shift` is removed from the `PayPeriod`, and then saved in the file it was parsed from.
