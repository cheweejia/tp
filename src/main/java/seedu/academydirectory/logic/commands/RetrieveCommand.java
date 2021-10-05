package seedu.academydirectory.logic.commands;


import static java.util.Objects.requireNonNull;
import static seedu.academydirectory.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.academydirectory.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.academydirectory.logic.parser.CliSyntax.PREFIX_PHONE;

import java.util.function.Function;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import seedu.academydirectory.model.Model;
import seedu.academydirectory.model.person.Information;
import seedu.academydirectory.model.person.Person;

/**
 * Finds and lists all persons in academy directory whose name contains any of the argument keywords.
 * Keyword matching is case-insensitive.
 */
public class RetrieveCommand extends Command {

    public static final String COMMAND_WORD = "retrieve";

    // TODO: Support for individual names
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Retrieve personal details of students "
            + "and displays them as a list\n"
            + "Parameters: " + PREFIX_ADDRESS + " | " + PREFIX_EMAIL + " | " + PREFIX_PHONE + "\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_PHONE;

    private final Function<? super Person, ? extends Information> filter;

    public RetrieveCommand(Function<? super Person, ? extends Information> filter) {
        this.filter = filter;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        ObservableList<Information> view = model.getFilteredPersonListView(filter);
        String result = view.stream().map(Object::toString).collect(Collectors.joining("\n"));
        return new CommandResult(result);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof RetrieveCommand // instanceof handles nulls
                && filter.equals(((RetrieveCommand) other).filter)); // state check
    }
}
