# RIT Shifts Tracker

A command line application for keeping track of my shifts worked in RIT Dining.

## [Shifts](./src/main/java/tracker/shifts/Shift.java)

**Definition: A period of time worked**

The Shift abstract class keeps track of:

- date
- time clocked in
- time clocked out
- pay rate

Child class are expected to represent a specific workplace in RIT Dining. Subclasses (*but not required to*) implement
a **Job enum** to keep track of the specific job worked during the shift, as well as the specific jobs available to a
workplace.

Current Shift subclasses (i.e. the places I've worked at in RIT Dining):

- [Global Village Cantina and Grille](./src/main/java/tracker/shifts/CGShift.java)
- [The Market at Global Village](./src/main/java/tracker/shifts/MarketShift.java)

## [PayPeriod](./src/main/java/tracker/shifts/PayPeriod.java)

**Definition: A two-week period that counts towards the amount received in the next pay check, always starts on a Friday
and ends on a Thursday**

The PayPeriod class keeps track of:

- start date
- end date
- total hours
- total earned
- shifts worked

The total hours worked and total earned are incremented with every Shift add to the ArrayList of Shifts in the
PayPeriod.

The total earned is the gross pay, not accounting for any amount withheld.

## Main Application

[The main application](./src/main/java/tracker/application/App.java) was developed to work specifically
with [Picocli](https://picocli.info/) to handle command line arguments.

```
Usage: RIT Dining Shift Tracker [-hV] [COMMAND]
A command line program for keeping track of my shifts worked in RIT Dining with JSON files.
  -h, --help      Show this help message and exit.
  -V, --version   Print version information and exit.
Commands:
  add     Add a shift to a Pay Period JSON file.
  new     Create a new Pay Period JSON file.
  edit    Edit a shift in a Pay Period JSON file.
  read    Read the contents of a Pay Period JSON file.
  remove  Remove a shift from a Pay Period JSON file.
```

The application takes user input to create Shifts and PayPeriods, then write the PayPeriod object as a JSON object,
using the [GSON](https://github.com/google/gson) library.
