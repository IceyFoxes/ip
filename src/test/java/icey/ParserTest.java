package icey;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import icey.command.AddCommand;
import icey.command.ByeCommand;
import icey.command.Command;
import icey.command.DeleteCommand;
import icey.command.ListCommand;
import icey.command.MarkCommand;
import icey.command.UnmarkCommand;

public class ParserTest {

    @Test
    public void parse_validTodo_returnsAddCommand() throws IceyException {
        Command command = Parser.parse("todo borrow book");
        assertInstanceOf(AddCommand.class, command);
    }

    @Test
    public void parse_emptyTodo_throwsException() {
        IceyException exception = assertThrows(IceyException.class, () -> Parser.parse("todo"));
        assertTrue(exception.getMessage().contains("description"));
    }

    @Test
    public void parse_validDeadline_returnsAddCommand() throws IceyException {
        Command command = Parser.parse("deadline return book /by 2019-12-02 1800");
        assertInstanceOf(AddCommand.class, command);
    }

    @Test
    public void parse_invalidDeadline_throwsException() {
        assertThrows(IceyException.class, () -> Parser.parse("deadline return book"));
    }

    @Test
    public void parse_validEvent_returnsAddCommand() throws IceyException {
        Command command = Parser.parse("event meeting /from 2019-12-02 1400 /to 2019-12-02 1600");
        assertInstanceOf(AddCommand.class, command);
    }

    @Test
    public void parse_invalidEvent_throwsException() {
        assertThrows(IceyException.class, () -> Parser.parse("event meeting /from /to"));
    }

    @Test
    public void parse_list_returnsListCommand() throws IceyException {
        assertInstanceOf(ListCommand.class, Parser.parse("list"));
    }

    @Test
    public void parse_mark_returnsMarkCommand() throws IceyException {
        assertInstanceOf(MarkCommand.class, Parser.parse("mark 1"));
    }

    @Test
    public void parse_unmark_returnsUnmarkCommand() throws IceyException {
        assertInstanceOf(UnmarkCommand.class, Parser.parse("unmark 1"));
    }

    @Test
    public void parse_delete_returnsDeleteCommand() throws IceyException {
        assertInstanceOf(DeleteCommand.class, Parser.parse("delete 1"));
    }

    @Test
    public void parse_bye_returnsByeCommand() throws IceyException {
        assertInstanceOf(ByeCommand.class, Parser.parse("bye"));
    }

    @Test
    public void parse_unknownCommand_throwsException() {
        assertThrows(IceyException.class, () -> Parser.parse("unknown"));
    }
}
