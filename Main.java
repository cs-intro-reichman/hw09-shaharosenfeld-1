public class Main {
    public static void main(String[] args) {
        // Create a new list
        List list = new List();

        // Test adding elements
        list.addFirst('_');
        list.addFirst('e');
        list.addFirst('e');
        list.addFirst('t');
        list.addFirst('t');
        list.addFirst('i');
        list.addFirst('m');
        list.addFirst('m');
        list.addFirst('o');
        list.addFirst('c');
        System.out.println("-----------------------");
        System.out.println(list);
        System.out.println("-----------------------");
        
        
        
        
        
        

        // Test updating elements
        list.update('e');
        list.update('t');

        // Test removing elements
        boolean removed = list.remove('x'); // Should return false since 'x' doesn't exist
        System.out.println("Removed 'x': " + removed);
        removed = list.remove('m'); // Should return true since 'm' exists
        System.out.println("Removed 'm': " + removed);

        // Test accessing elements
        System.out.println("Element at index 2: " + list.get(2));

        // Test converting to array
        CharData[] array = list.toArray();
        System.out.print("List as array: ");
        for (CharData data : array) {
            System.out.print(data + " ");
        }
        System.out.println();

        // Test iterating through the list
        ListIterator iterator = list.listIterator(0);
        System.out.print("List elements: ");
        while (iterator.hasNext()) {
            System.out.print(iterator.next().toString() + " ");
        }
        System.out.println();
    }
}
