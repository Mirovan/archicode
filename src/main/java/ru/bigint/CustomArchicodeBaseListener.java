package ru.bigint;

import org.antlr.v4.runtime.misc.Pair;
import ru.bigint.model.DiagObject;
import ru.bigint.model.Diagram;
import ru.bigint.model.Relation;
import ru.bigint.model.RelationDirection;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

public class CustomArchicodeBaseListener extends ArchicodeBaseListener {
    private Diagram diagram;
    private Queue<Pair<Object, Object>> pairs = new LinkedList<>();

    public CustomArchicodeBaseListener(Diagram diagram) {
        this.diagram = diagram;
    }

    @Override
    public void exitObject(ArchicodeParser.ObjectContext ctx) {
        //Завершаем чтение объекта и сохраняем его свойства
        if (ctx.IDENTIFIER() != null) {
            DiagObject obj = findOrCreate(ctx.IDENTIFIER().getText());
            diagram.addObject(obj);
            obj.addProperties(pairs);
            pairs.clear();
        }
    }

    @Override
    public void exitPair(ArchicodeParser.PairContext ctx) {
        pairs.add(new Pair(ctx.key(), ctx.value()));
        super.exitPair(ctx);
    }

    @Override
    public void exitRelation(ArchicodeParser.RelationContext ctx) {
        DiagObject fromObj = null;
        DiagObject toObj = null;
        RelationDirection relDirectionFrom = null;
        RelationDirection relDirectionTo = null;
        RelationDirection toObjectPriorityPosition = null;

        //Объекты - откуда и куда
        if (ctx.object() != null && ctx.object().size() == 2) {
            fromObj = findOrCreate(ctx.object(0).IDENTIFIER().getText());
            toObj = findOrCreate(ctx.object(1).IDENTIFIER().getText());
        }

        //Направление связи
        for (var pair : pairs) {
            String key = ((ArchicodeParser.KeyContext) pair.a).IDENTIFIER().getText();
            String value = ((ArchicodeParser.ValueContext) pair.b).getText();
            value = value.substring(1, value.length()-1);

            if ("directionFrom".equals(key)) {
                relDirectionFrom = RelationDirection.fromString(value);
                if (relDirectionFrom == null) relDirectionFrom = RelationDirection.RIGHT;
            }

            if ("directionTo".equals(key)) {
                relDirectionTo = RelationDirection.fromString(value);
                if (relDirectionTo == null) relDirectionTo = RelationDirection.LEFT;
            }
        }
//        if (ctx.relationDirections() != null
//                && ctx.relationDirections().direction().size() == 2) {
//            relDirectionFrom = RelationDirection.fromString(ctx.relationDirections().direction(0).getText());
//            relDirectionTo = RelationDirection.fromString(ctx.relationDirections().direction(1).getText());
//        }
//        if (ctx.priorityPosition() != null) {
//            toObjectPriorityPosition = RelationDirection.fromString(ctx.priorityPosition().getText());
//        }

        Relation relation = new Relation(fromObj, toObj, relDirectionFrom, relDirectionTo, toObjectPriorityPosition);
        diagram.addObject(fromObj);
        diagram.addObject(toObj);
        diagram.addRelation(relation);
    }

    private DiagObject findOrCreate(String text) {
        Optional<DiagObject> obj = diagram.getObjects().stream()
                .filter(item -> item != null && item.getName().equals(text))
                .findFirst();
        return obj.orElseGet(() -> new DiagObject(text));
    }

}
