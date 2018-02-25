package concurrency.without.pain.collections;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CopyOnWriteArrayListDemo {

    public static void main(String[] args) {
        List<String> empList = new ArrayList<>();
        empList.add("John Doe");
        empList.add("John Doe");
        empList.add("Rita Smith");

        Iterator<String> it = empList.iterator();

        while (it.hasNext()) {
            try {
                System.out.println(it.next());
                if (!empList.contains("Tom Smith"))
                    empList.add("Tom Smith");
            } catch (ConcurrentModificationException cme) {
                System.err.println("attempt to modify list during iteration");
                break;
            }
        }

        empList = new CopyOnWriteArrayList<>();
        empList.add("John Doe");
        empList.add("Jane Doe");
        empList.add("Rita Smith");
        it = empList.iterator();

        while (it.hasNext()) {
            System.out.println(it.next());
            if (!empList.contains("Tom Smith"))
                empList.add("Tom Smith");
        }
    }
}
