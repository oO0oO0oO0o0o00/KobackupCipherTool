package commands;

import com.github.rvesse.airline.parser.ParseResult;
import com.github.rvesse.airline.parser.ParseState;
import com.github.rvesse.airline.parser.errors.ParseException;
import com.github.rvesse.airline.parser.errors.handlers.ParserErrorHandler;

import java.util.ArrayList;
import java.util.List;

public class ExceptionHandler implements ParserErrorHandler {
    private final List<ParseException> errors = new ArrayList<>();

    @Override
    public void handleError(ParseException e) {
        errors.add(e);
    }

    @Override
    public <T> ParseResult<T> finished(ParseState<T> parseState) {
        return new ParseResult<>(parseState, errors);
    }
}