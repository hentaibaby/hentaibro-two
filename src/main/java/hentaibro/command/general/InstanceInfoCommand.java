package hentaibro.command.general;

import hentaibro.command.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import oshi.SystemInfo;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HardwareAbstractionLayer;

import java.awt.*;
import java.util.Collections;
import java.util.List;

public class InstanceInfoCommand extends Command
{
    @Override
    public String getDescription()
    {
        return "```*instance\n\n" +
                "Returns information about the runtime environment of the bot.```";
    }

    @Override
    public void onCommand(MessageReceivedEvent event, String[] args)
    {
        SystemInfo si = new SystemInfo();

        HardwareAbstractionLayer hi = si.getHardware();

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.RED);
        eb.setTitle("Host Machine Info");

        String sb = "";

        sb += "Host OS: " + si.getOperatingSystem() + "\n";
        sb += "Manufacturer: " + hi.getComputerSystem().getManufacturer() + "\n";
        sb += "Model: " + hi.getComputerSystem().getModel() + "\n";
        sb += "Motherboard: " + hi.getComputerSystem().getBaseboard().getModel() + "\n";
        sb += "Firmware: " + hi.getComputerSystem().getFirmware().getName() + "\n";
        sb += "Uptime: " + hi.getProcessor().getSystemUptime();

        eb.addField("General info", sb, false);

        sb = "Model: " + hi.getProcessor().getName() + "\n";
        sb += "kikes: " + hi.getProcessor().getVendor();

        eb.addField("CPU info", sb, false);

        sb = "Available Memory (MB): " + hi.getMemory().getAvailable() / 1048576 + "\n";
        sb += "Total Memory (MB): " + hi.getMemory().getTotal() / 1048576 + "\n";
        sb += "Memory Usage(MB): " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576;


        eb.addField("Memory info", sb, false);

        HWDiskStore[] disks = hi.getDiskStores();


        long size = 0;

        for (HWDiskStore d : disks)
        {
            size += d.getSize() / 1073741824;
        }

        sb = "# Disks: " + disks.length + "\n" + "Size (GB): " + size;

        eb.addField("Disk info", sb, false);
        channel.sendMessage(eb.build()).queue();

    }

    @Override
    public List<String> getAliases()
    {
        return Collections.singletonList(pf + "instance");

    }
}
