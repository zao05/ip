# Penny User Guide

Penny is a lightweight, CLI-first task management desktop application optimized for users who prefer fast typed commands over graphical interactions.

---

## Features

### 1. Adding Tasks

#### A. Todo: `todo`
Adds a basic task without any date or time constraints.

* **Format:** `todo <description>`
* **Example:** `todo read biology textbook`
* **Output:**
  ```text
  Got it. I've added this task:
    [T][ ] read biology textbook
  Now you have 1 tasks in the list.
  ```

#### B. Deadline: `deadline`
Adds a task that must be completed by a specific date or date-time.

* **Format:** `deadline <description> /by <yyyy-MM-dd | d/M/yyyy [HHmm]>`
* **Example:** `deadline submit assignment /by 2026-10-15`
* **Output:**
  ```text
  Got it. I've added this task:
    [D][ ] submit assignment (by: Oct 15 2026)
  Now you have 2 tasks in the list.
  ```

#### C. Event: `event`
Adds an event spanning from a start time to an end time.

* **Format:** `event <description> /from <start> /to <end>`
* **Example:** `event project meeting /from 2026-10-20 1400 /to 2026-10-20 1600`
* **Output:**
  ```text
  Got it. I've added this task:
    [E][ ] project meeting (from: Oct 20 2026, 2:00pm to: Oct 20 2026, 4:00pm)
  Now you have 3 tasks in the list.
  ```

---

### 2. Finding Tasks by Keywords: `find` (Feature: `C-BetterSearch`)

Finds tasks in your list using flexible, case-insensitive keyword searching.

* **Format:** `find <KEYWORD> [ADDITIONAL_KEYWORDS]...`
* **Capabilities:**
  * **Partial Matching:** Matches parts of words (e.g. `bio` matches `biology`, `text` matches `textbook`).
  * **Multi-Keyword Search (AND matching):** When multiple space-separated words are provided, Penny searches for tasks that contain **all** specified keywords, in any order.
  * **Case-Insensitive:** Works transparently regardless of uppercase or lowercase characters.
* **Examples:**
  * `find bio` $\rightarrow$ Finds all tasks containing `bio` (such as `biology`).
  * `find bio text` $\rightarrow$ Finds tasks containing both `bio` and `text` (such as `read biology textbook`).
  * `find textbook read` $\rightarrow$ Finds `read biology textbook` (order-independent).
* **Output (Matches Found):**
  ```text
  Here are the matching tasks in your list:
  1.[T][ ] read biology textbook
  ```
* **Output (No Matches Found):**
  ```text
  No matching tasks found for keyword: 'chemistry'.
  ```

---

### 3. Finding Tasks on a Specific Date: `on`

Lists all deadlines and events occurring on a specific date.

* **Format:** `on <yyyy-MM-dd | d/M/yyyy>`
* **Example:** `on 2026-10-15`

---

### 4. Managing Tasks

* **List all tasks:** `list`
* **Mark as completed:** `mark <task_number>` (e.g., `mark 1`)
* **Mark as uncompleted:** `unmark <task_number>` (e.g., `unmark 1`)
* **Delete a task:** `delete <task_number>` (e.g., `delete 1`)

---

### 5. Exiting: `bye`

Exits the Penny chatbot.

* **Format:** `bye`