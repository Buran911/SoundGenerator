package com.buran911;

import com.buran911.music.Composition;
import com.buran911.music.Note;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

/**
 * Создатель композиции из xml-структуры.
 */
@NoArgsConstructor(access = AccessLevel.NONE)
public abstract class XmlCompositionReader {

    /**
     * Читает композицию из xml-файла.
     */
    public static Composition readComposition(Path compositionFile) {
        Document doc = readXmlFile(compositionFile);
        return createComposition(doc);
    }

    /**
     * Читает xml-файл в документ.
     */
    private static Document readXmlFile(Path xmlFile) {
        try {
            return DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(xmlFile.toFile());
        } catch (Exception ex) {
            throw new RuntimeException(format("Unable to parse xml-file %s", xmlFile));
        }
    }

    /**
     * Строит композицию из xml-документа.
     */
    private static Composition createComposition(Document doc) {
        try {
            return visitNode(doc, 0, new Composition());
        } catch (Exception ex) {
            throw new RuntimeException("Unable to create composition from xml-data");
        }
    }

    /**
     * Посещает узел xml-файла и заполняет композицию.
     */
    private static Composition visitNode(Node node, int level, Composition composition) {
        composition = processNode(node, composition);

        if (!"music".equals(node.getNodeName())) {                    // ноты обходятся отдельно в обработке узла
            NodeList childNodes = node.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                composition = visitNode(childNodes.item(i), level + 1, composition);
            }
        }

        return composition;
    }

    /**
     * Вытаскивает данные из узла и добавляет их в композицию.
     */
    private static Composition processNode(Node node, Composition composition) {
        String nodeValue = node.getTextContent();
        switch (node.getNodeName()) {
            case "name":
                composition.setName(nodeValue);
                break;
            case "composer":
                composition.setComposer(nodeValue);
                break;
            case "temp":
                composition.setTemp(Integer.parseInt(nodeValue));
                break;
            case "music":
                composition.setNotes(extractNotes(node));
                break;
        }
        return composition;
    }

    /**
     * Достает список нот из соответствующего узла.
     */
    private static List<Note> extractNotes(Node node) {
        List<Note> notes = new ArrayList<>();
        NodeList childNodes = ((Element) node).getElementsByTagName("note");
        for (int i = 0; i < childNodes.getLength(); i++) {
            notes.add(createNote(childNodes.item(i)));
        }
        return notes;
    }

    /**
     * Создает ноту из соответствующего xml-узла.
     */
    private static Note createNote(Node noteNode) {
        return new Note(
                ((Element) noteNode).getElementsByTagName("tone").item(0).getTextContent(),
                ((Element) noteNode).getElementsByTagName("duration").item(0).getTextContent()
        );
    }
}