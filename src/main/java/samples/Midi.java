package samples;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;

/**
 * Created by IntelliJ IDEA.
 * User: jpc
 * Date: Oct 25, 2004
 * Time: 9:51:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class Midi {
    public static void main(String[] args) throws Exception {
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for (int i = 0; i < infos.length; i++){
            System.out.println("" + i + ": " + infos[i]);
            MidiDevice midiDevice = MidiSystem.getMidiDevice(infos[i]);
            midiDevice.open();
            if (midiDevice.getMaxReceivers() > 0) System.out.println(" " + midiDevice.getReceiver());
            if (midiDevice.getMaxTransmitters() > 0) System.out.println(" " + midiDevice.getTransmitter());
            midiDevice.close();
        }
    }
}
