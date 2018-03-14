/**
 * 
 *    Copyright 2017 Florian Erhard
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 * 
 */
/*
 * Copyright (c) 2002-2015, the original author or authors.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package jline.console;

/**
 * List of all operations.
 *
 * @author <a href="mailto:gnodet@gmail.com">Guillaume Nodet</a>
 * @since 2.6
 */
public enum Operation {

    ABORT,
    ACCEPT_LINE,
    ARROW_KEY_PREFIX,
    BACKWARD_BYTE,
    BACKWARD_CHAR,
    BACKWARD_DELETE_CHAR,
    BACKWARD_KILL_LINE,
    BACKWARD_KILL_WORD,
    BACKWARD_WORD,
    BEGINNING_OF_HISTORY,
    BEGINNING_OF_LINE,
    CALL_LAST_KBD_MACRO,
    CAPITALIZE_WORD,
    CHARACTER_SEARCH,
    CHARACTER_SEARCH_BACKWARD,
    CLEAR_SCREEN,
    COMPLETE,
    COPY_BACKWARD_WORD,
    COPY_FORWARD_WORD,
    COPY_REGION_AS_KILL,
    DELETE_CHAR,
    DELETE_CHAR_OR_LIST,
    DELETE_HORIZONTAL_SPACE,
    DIGIT_ARGUMENT,
    DO_LOWERCASE_VERSION,
    DOWNCASE_WORD,
    DUMP_FUNCTIONS,
    DUMP_MACROS,
    DUMP_VARIABLES,
    EMACS_EDITING_MODE,
    END_KBD_MACRO,
    END_OF_HISTORY,
    END_OF_LINE,
    EXCHANGE_POINT_AND_MARK,
    EXIT_OR_DELETE_CHAR,
    FORWARD_BACKWARD_DELETE_CHAR,
    FORWARD_BYTE,
    FORWARD_CHAR,
    FORWARD_SEARCH_HISTORY,
    FORWARD_WORD,
    HISTORY_SEARCH_BACKWARD,
    HISTORY_SEARCH_FORWARD,
    INSERT_CLOSE_CURLY,
    INSERT_CLOSE_PAREN,
    INSERT_CLOSE_SQUARE,
    INSERT_COMMENT,
    INSERT_COMPLETIONS,
    INTERRUPT,
    KILL_WHOLE_LINE,
    KILL_LINE,
    KILL_REGION,
    KILL_WORD,
    MENU_COMPLETE,
    MENU_COMPLETE_BACKWARD,
    NEXT_HISTORY,
    NON_INCREMENTAL_FORWARD_SEARCH_HISTORY,
    NON_INCREMENTAL_REVERSE_SEARCH_HISTORY,
    NON_INCREMENTAL_FORWARD_SEARCH_HISTORY_AGAIN,
    NON_INCREMENTAL_REVERSE_SEARCH_HISTORY_AGAIN,
    OLD_MENU_COMPLETE,
    OVERWRITE_MODE,
    PASTE_FROM_CLIPBOARD,
    POSSIBLE_COMPLETIONS,
    PREVIOUS_HISTORY,
    QUOTED_INSERT,
    QUIT,
    RE_READ_INIT_FILE,
    REDRAW_CURRENT_LINE,
    REVERSE_SEARCH_HISTORY,
    REVERT_LINE,
    SELF_INSERT,
    SET_MARK,
    SKIP_CSI_SEQUENCE,
    START_KBD_MACRO,
    TAB_INSERT,
    TILDE_EXPAND,
    TRANSPOSE_CHARS,
    TRANSPOSE_WORDS,
    TTY_STATUS,
    UNDO,
    UNIVERSAL_ARGUMENT,
    UNIX_FILENAME_RUBOUT,
    UNIX_LINE_DISCARD,
    UNIX_WORD_RUBOUT,
    UPCASE_WORD,
    YANK,
    YANK_LAST_ARG,
    YANK_NTH_ARG,
    YANK_POP,
    VI_APPEND_EOL,
    VI_APPEND_MODE,
    VI_ARG_DIGIT,
    VI_BACK_TO_INDENT,
    VI_BACKWARD_BIGWORD,
    VI_BACKWARD_WORD,
    VI_BWORD,
    VI_CHANGE_CASE,
    VI_CHANGE_CHAR,
    VI_CHANGE_TO,
    VI_CHANGE_TO_EOL,
    VI_CHAR_SEARCH,
    VI_COLUMN,
    VI_COMPLETE,
    VI_DELETE,
    VI_DELETE_TO,
    VI_DELETE_TO_EOL,
    VI_EDITING_MODE,
    VI_END_BIGWORD,
    VI_END_WORD,
    VI_EOF_MAYBE,
    VI_EWORD,
    VI_FWORD,
    VI_FETCH_HISTORY,
    VI_FIRST_PRINT,
    VI_FORWARD_BIGWORD,
    VI_FORWARD_WORD,
    VI_GOTO_MARK,
    VI_INSERT_BEG,
    VI_INSERTION_MODE,
    VI_KILL_WHOLE_LINE,
    VI_MATCH,
    VI_MOVEMENT_MODE,
    VI_NEXT_WORD,
    VI_OVERSTRIKE,
    VI_OVERSTRIKE_DELETE,
    VI_PREV_WORD,
    VI_PUT,
    VI_REDO,
    VI_REPLACE,
    VI_RUBOUT,
    VI_SEARCH,
    VI_SEARCH_AGAIN,
    VI_SET_MARK,
    VI_SUBST,
    VI_TILDE_EXPAND,
    VI_YANK_ARG,
    VI_YANK_TO,
    VI_MOVE_ACCEPT_LINE,
    VI_NEXT_HISTORY,
    VI_PREVIOUS_HISTORY,
    VI_INSERT_COMMENT,
    VI_BEGINNING_OF_LINE_OR_ARG_DIGIT,
}
