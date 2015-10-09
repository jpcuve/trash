package api.jaf;

import javax.activation.CommandInfo;
import javax.activation.DataHandler;
import java.awt.datatransfer.DataFlavor;

/**
 * @author jpc
 */
public class Client {
    public static void main(String[] args) throws Exception {
        final DataHandler dataHandler = new DataHandler(new ObjectDataSource<Brol>(new Brol(), Brol.OBJECT_DATA_FLAVOR.getMimeType()));
        final CommandInfo[] commands = dataHandler.getAllCommands();
        for (final CommandInfo commandInfo: commands){
            System.out.printf("command info: %s %s%n", commandInfo.getCommandName(), commandInfo.getCommandClass());
        }
        for (final DataFlavor dataFlavor: dataHandler.getTransferDataFlavors()){
            System.out.printf("transfer data flavor: %s%n", dataFlavor.getMimeType());
        }
        if (commands.length > 0){
            final Object o = dataHandler.getCommand("view").getCommandObject(dataHandler, null);
        }

    }
}
