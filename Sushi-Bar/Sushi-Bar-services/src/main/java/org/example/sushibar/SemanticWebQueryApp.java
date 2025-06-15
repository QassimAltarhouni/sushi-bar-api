package org.example.sushibar;

import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.*;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class SemanticWebQueryApp {

    public static void main(String[] args) throws Exception {
        Repository repo = new SailRepository(new MemoryStore());
        repo.init();

        try (RepositoryConnection conn = repo.getConnection()) {
            // ✅ Load RDF files using getResourceAsStream
            conn.add(getStream("sushi-bar-semantic-web/data/ontology.ttl"), "", RDFFormat.TURTLE);
            conn.add(getStream("sushi-bar-semantic-web/data/instances.ttl"), "", RDFFormat.TURTLE);

            // ✅ Load SPARQL queries from file
            InputStream queryStream = getStream("sushi-bar-semantic-web/data/queries.sparql");
            List<String> lines = new BufferedReader(new InputStreamReader(queryStream))
                    .lines().collect(Collectors.toList());

            int queryCounter = 1;
            StringBuilder currentQuery = new StringBuilder();

            for (String line : lines) {
                if (line.trim().startsWith("# Query")) {
                    if (currentQuery.length() > 0) {
                        runQuery(conn, currentQuery.toString(), queryCounter++);
                        currentQuery.setLength(0);
                    }
                    System.out.println("\n" + line);
                } else {
                    currentQuery.append(line).append("\n");
                }
            }

            // Run the last query
            if (currentQuery.length() > 0) {
                runQuery(conn, currentQuery.toString(), queryCounter);
            }
        }

        repo.shutDown();
    }

    private static InputStream getStream(String path) {
        InputStream stream = SemanticWebQueryApp.class.getClassLoader().getResourceAsStream(path);
        if (stream == null) {
            throw new RuntimeException("File not found: " + path);
        }
        return stream;
    }

    private static void runQuery(RepositoryConnection conn, String query, int index) {
        try {
            TupleQuery tupleQuery = conn.prepareTupleQuery(QueryLanguage.SPARQL, query);
            try (TupleQueryResult result = tupleQuery.evaluate()) {
                System.out.println("→ Query " + index + " Results:");
                while (result.hasNext()) {
                    BindingSet set = result.next();
                    for (String name : set.getBindingNames()) {
                        Value value = set.getValue(name);
                        System.out.print(name + ": " + value.stringValue() + " | ");
                    }
                    System.out.println();
                }
            }
        } catch (Exception e) {
            System.out.println("✖ Failed to run query " + index + ": " + e.getMessage());
        }
    }
}
