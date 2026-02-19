# Icey User Guide

Icey is a lightweight task-tracking chatbot with a CLI and JavaFX GUI.
It helps you capture tasks quickly, manage progress, and search your list.

## Quick start

1. Launch Icey.
2. Enter commands in the input box (GUI) or terminal (CLI).
3. Use `list` to view current tasks.

## Command summary

| Action | Command format |
| --- | --- |
| Add todo | `todo <description>` |
| Add deadline | `deadline <description> /by <yyyy-MM-dd HHmm>` |
| Add event | `event <description> /from <yyyy-MM-dd HHmm> /to <yyyy-MM-dd HHmm>` |
| List tasks | `list` |
| Mark done | `mark <task number>` |
| Mark not done | `unmark <task number>` |
| Delete task | `delete <task number>` |
| Find tasks | `find <keyword>` |
| Tag task | `tag <task number> <tag>` |
| Exit Icey | `bye` |

## Features

### 1) Add a todo

Adds a basic task without date/time.

Example:
`todo borrow book`

### 2) Add a deadline

Adds a task with a due date and time.

Example:
`deadline return book /by 2019-12-02 1800`

### 3) Add an event

Adds a task with start and end date/time.

Example:
`event project meeting /from 2019-12-02 1400 /to 2019-12-02 1600`

### 4) List tasks

Shows all tasks with numbering. Use this to find task numbers for `mark`, `unmark`, `delete`, and `tag`.

Example:
`list`

### 5) Mark / unmark

Marks a task as done or not done.

Examples:
- `mark 2`
- `unmark 2`

### 6) Delete a task

Removes a task by its displayed number.

Example:
`delete 3`

### 7) Find tasks

Finds tasks containing a keyword (case-insensitive).

Example:
`find book`

### 8) Tag tasks

Adds a tag to a task. If your tag does not start with `#`, Icey adds it automatically.

Examples:
- `tag 1 #school`
- `tag 1 urgent` (saved as `#urgent`)

### 9) Exit

Ends the session.

Example:
`bye`

## Notes and tips

- Task numbers are based on the current `list` output.
- Date-time input must use `yyyy-MM-dd HHmm`.
- `list` and `bye` do not accept extra arguments.
- If a command format is wrong, Icey will show a usage hint.