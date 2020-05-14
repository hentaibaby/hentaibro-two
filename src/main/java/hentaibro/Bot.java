package hentaibro;

import hentaibro.command.NListener;
import hentaibro.command.TestCommand;
import hentaibro.command.faptrack.*;
import hentaibro.command.faptrack.hentai.AnalyzeDoujinCommand;
import hentaibro.command.faptrack.hentai.GetQuoteCommand;
import hentaibro.command.faptrack.hentai.HentaiSearchCommand;
import hentaibro.command.faptrack.hentai.RecalculateDegeneracyCommand;
import hentaibro.command.faptrack.porno.AnalyzePornoCommand;
import hentaibro.command.general.*;
import hentaibro.command.meme.*;
import hentaibro.database.BackupManager;
import hentaibro.database.Database;
import hentaibro.util.CooldownManager;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.sql.SQLException;

public class Bot
{

    //Non error, no action exit codes.
    public static final int NORMAL_SHUTDOWN = 10;
    public static final int NEWLY_CREATED_CONFIG = 12;

    //Non error, action required exit codes.
    public static final File directory = new File(Bot.class.getProtectionDomain()
            .getCodeSource().getLocation().getFile()).getParentFile();
    private static final Logger logger = LoggerFactory.getLogger(Bot.class);
    //error exit codes.
    private static final int UNABLE_TO_CONNECT_TO_DISCORD = 30;
    private static final int BAD_USERNAME_PASS_COMBO = 31;
    private static final int NO_USERNAME_PASS_COMBO = 32;
    private static final int DATABASE_ERROR = 33;
    public static String selfID;
    public static JDA api;

    public static void main(String[] args)
    {
        api = startBot();
    }

    public static void restartBot()
    {
        api.shutdown();
        api = startBot();
    }

    private static JDA startBot()
    {
        try
        {
            Database.getInstance().checkTables();
            BackupManager.startBackupService();
            CooldownManager.clearCooldowns();
            SettingsManager.loadSettings();

            JDABuilder jdaBuilder = new JDABuilder(AccountType.BOT).setToken(Settings.botToken);
            HelpCommand help = new HelpCommand();

            jdaBuilder.addEventListeners(help.registerCommand(help));

            //general
            jdaBuilder.addEventListeners(help.registerCommand(new InstanceInfoCommand()));

            //undocumented
            jdaBuilder.addEventListeners(new WhitelistCommand());
            jdaBuilder.addEventListeners(new BlacklistCommand());
            jdaBuilder.addEventListeners(new CheckListCommand());
            jdaBuilder.addEventListeners(new SendMessage());
            jdaBuilder.addEventListeners(new StopCommand());
            jdaBuilder.addEventListeners(new SQLCommand());
            jdaBuilder.addEventListeners(new ScannerThread());
            jdaBuilder.addEventListeners(new ShellCommand());

            //hentai
            jdaBuilder.addEventListeners(help.registerCommand(new AnalyzeDoujinCommand()));
            jdaBuilder.addEventListeners(help.registerCommand(new HentaiSearchCommand()));
            //jdaBuilder.addEventListeners(help.registerCommand(new RandomDoujinCommand()));
            jdaBuilder.addEventListeners(help.registerCommand(new FapTrackCommand()));
            jdaBuilder.addEventListeners(help.registerCommand(new HentaiProfileCommand()));
            jdaBuilder.addEventListeners(help.registerCommand(new RecentFapsCommand()));
            jdaBuilder.addEventListeners(help.registerCommand(new CommonFapsCommand()));
            jdaBuilder.addEventListeners(help.registerCommand(new FapTopCommand()));
            jdaBuilder.addEventListeners(help.registerCommand(new CompareCommand()));
            jdaBuilder.addEventListeners(help.registerCommand(new DegenTopCommand()));
            jdaBuilder.addEventListeners(help.registerCommand(new HentaiGraphCommand()));
            jdaBuilder.addEventListeners(help.registerCommand(new TEMPFAP()));

            jdaBuilder.addEventListeners(new RecalculateDegeneracyCommand());
            jdaBuilder.addEventListeners(new AddDegenTagCommand());
            jdaBuilder.addEventListeners(new SearchTagCommand());

            jdaBuilder.addEventListeners(new GetQuoteCommand());


            //porno
            jdaBuilder.addEventListeners(help.registerCommand(new AnalyzePornoCommand()));
            // jdaBuilder.addEventListeners(help.registerCommand(new PHFapTrackCommand()));

            //meme
            jdaBuilder.addEventListeners(new NameChangeCommand());
            jdaBuilder.addEventListeners(new NListener());
            jdaBuilder.addEventListeners(new NiggerTopCommand());



            jdaBuilder.addEventListeners(new BackupCommand());
            jdaBuilder.addEventListeners(new NCommand());

            help.build();
            api = jdaBuilder.build();
            api.getPresence().setActivity(Activity.listening("😂₦ł₲Ⱨ₮₵ØⱤɆ🕋"));
            return api;

            //selfID = api.getSelfUser().getId();

        }
        catch (IllegalArgumentException e)
        {
            System.out.println("No login details provided! Please provide a botToken in the config.");
            System.exit(NO_USERNAME_PASS_COMBO);
        }
        catch (LoginException e)
        {
            System.out.println("The botToken provided in the Config.json was incorrect.");
            System.out.println("Did you modify the Config.json after it was created?");
            System.exit(BAD_USERNAME_PASS_COMBO);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.exit(DATABASE_ERROR);
        }
        return null;
    }

}
