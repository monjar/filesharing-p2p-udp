package database;


import database.model.Node;
import org.junit.Test;

import static org.junit.Assert.*;
import java.util.List;

public class DatabaseTest {
    @Test
    public void testLoadAll(){
        try {
            DatabaseConnector.getInstance().clear(Node.class);
            Node n1 = new Node("TestAddress1",0,0);
            Node n2 = new Node("TestAddress2",0,0);
            DatabaseConnector.getInstance().save(n1);
            DatabaseConnector.getInstance().save(n2);
            List<Node> list= DatabaseConnector.getInstance().loadAll(Node.class);
            assertEquals(list.get(0).getAddress(),n1.getAddress());
            assertEquals(list.get(1).getAddress(),n2.getAddress());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

    }

    @Test
    public void testLoadOneId(){
        try {
            DatabaseConnector.getInstance().clear(Node.class);
            Node n1 = new Node("TestAddress3",0,0);
            String id = DatabaseConnector.getInstance().save(n1);
            Node n2 = DatabaseConnector.getInstance().loadOneById(Node.class, id);
            assertEquals(n2.getAddress(),n1.getAddress());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

    }
}
