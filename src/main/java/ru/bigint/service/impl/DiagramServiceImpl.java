package ru.bigint.service.impl;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.springframework.stereotype.Service;
import ru.bigint.ArchicodeLexer;
import ru.bigint.ArchicodeParser;
import ru.bigint.CustomArchicodeBaseListener;
import ru.bigint.model.Diagram;
import ru.bigint.service.DiagramService;

import java.io.IOException;
import java.io.InputStream;

@Service
public class DiagramServiceImpl implements DiagramService {
    @Override
    public String createDiagram(String data) {
        data = "Diagram {" + data + "}";
        //Loading the DSL script into the ANTLR stream.
        CharStream cs = CharStreams.fromString(data);

        //Passing the input to the lexer to create tokens
        ArchicodeLexer lexer = new ArchicodeLexer(cs);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        //Passing the tokens to the parser to create the parse trea.
        ArchicodeParser parser = new ArchicodeParser(tokens);

        //Semantic model to be populated
        Diagram diagram = new Diagram();

        //Adding the listener to facilitate walking through parse tree.
        parser.addParseListener(new CustomArchicodeBaseListener(diagram));

        //invoking the parser.
        parser.diagram();

        return diagram.draw();
    }
}
