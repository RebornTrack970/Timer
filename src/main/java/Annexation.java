import arc.Events;
import arc.struct.Seq;
import arc.util.Align;
import arc.util.Timer;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.game.Teams.*;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.mod.Plugin;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static java.util.Collections.reverse;

public class Annexation extends Plugin {
    HashMap<Team, Integer> scores = new HashMap<>();
    HashMap<Team, Integer> lastIncrease = new HashMap<>();

    int winScore = -1;
    int updateInterval = -1;
    int topLength = -1;

    @Override
    public void init() {

        //load config
        Properties props = new Properties();
        try(InputStream resourceStream = Annexation.class.getResourceAsStream("config.properties")) {
            props.load(resourceStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        winScore = Integer.parseInt(props.getProperty("winScore"));
        updateInterval = Integer.parseInt(props.getProperty("updateInterval"));
        topLength = Integer.parseInt(props.getProperty("topLength"));

	Timer.schedule(() -> {
	    String progress = "[cyan]Timer: " + winScore;
	    winScore -= updateInterval;
	    Call.infoPopup(progress, updateInterval, Align.bottom, 0, 0, 0, 0);
	    if(winScore<1){
		Events.fire(new EventType.GameOverEvent(Team.derelict));
	    }
            Events.on(EventType.WorldLoadEvent.class, e -> {
                winScore = Integer.parseInt(props.getProperty("winScore"));
                lastIncrease.clear();
            });
	}, 0, updateInterval);
    }
}
