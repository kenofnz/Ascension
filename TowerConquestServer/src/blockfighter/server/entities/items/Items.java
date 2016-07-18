package blockfighter.server.entities.items;

import blockfighter.server.Globals;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

public class Items {

    public static final HashSet<Integer> ITEM_CODES = new HashSet<>();
    public static final HashSet<Integer> ITEM_UPGRADE_CODES = new HashSet<>();

    static {
        loadItemCodes();
    }

    public static void loadItemCodes() {
        ITEM_UPGRADE_CODES.add(100);
        try {
            InputStream itemFile = Globals.loadResourceAsStream("itemcodes.txt");
            LineIterator it = IOUtils.lineIterator(itemFile, "UTF-8");
            try {
                while (it.hasNext()) {
                    String line = it.nextLine();
                    try {
                        int itemcode = Integer.parseInt(line);
                        ITEM_CODES.add(itemcode);
                    } catch (NumberFormatException e) {
                    }
                }
            } finally {
                LineIterator.closeQuietly(it);
            }
            Globals.log(Items.class, "Item Codes loaded: Equips: " + Arrays.toString(ITEM_CODES.toArray()) + " Upgrades: " + Arrays.toString(ITEM_UPGRADE_CODES.toArray()), Globals.LOG_TYPE_DATA, true);
        } catch (Exception e) {
            Globals.logError("Could not load item codes from data", e, true);
        }
    }
}
