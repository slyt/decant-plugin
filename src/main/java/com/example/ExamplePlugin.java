package com.example;

import com.google.inject.Provides;
import jaco.mp3.player.MP3Player;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.NPC;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;
import java.util.HashMap;

@Slf4j
@PluginDescriptor(
	name = "Example"
)
public class ExamplePlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ExampleConfig config;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Example started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Example stopped!");
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Example says " + config.greeting(), null);
		}
	}

	private HashMap<Integer, Integer> animationTable = new HashMap<>();
	int vanstromSpecialCount = 0;
	int vanstromNormalCount = 0;
	private MP3Player customtrack = new MP3Player(getClass().getClassLoader().getResource("run_away_little_girl.mp3"));
	int guardSwordAttackCount = 0;

	@Subscribe
	public void onAnimationChanged(AnimationChanged e)
	{
		if (!(e.getActor() instanceof  NPC)) // Only look for NPC animators
			return;
		NPC animatorActor = (NPC) e.getActor();
		int npcId = animatorActor.getId();

		String npcName = animatorActor.getName();
		int npcLevel = animatorActor.getCombatLevel();
		int animationID = animatorActor.getAnimation();

		if (npcId == -1 || animationID == -1)
			return; // Don't bother with null NPC and idle animations

//		// Update animation table hashmap
//		int targetNpcId = 9569; // Vanstrom Klause
//		if (npcId == targetNpcId) {
//			animationTable.put(animationID, animationTable.getOrDefault(animationID, 0) + 1);
//			log.info("Animation table: " + animationTable.toString());
//			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Animation table: " + animationTable.toString(), null);
//
////			String attackText = animatorActor.getOverheadText(); // Get text used for each attack.
////			switch (attackText){
////				case "My pets will feast on your corpse!":
////					client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Range the bloodveld!", null);
////					vanstromNormalCount = 0; // reset normal attack counter after special is used
////				case "Blood will be my strength!":
////					client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Kite Klaus into the blood spore!", null);
////					vanstromNormalCount = 0; // reset normal attack counter after special is used
////				case "Stare into darkness!":
////					client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Don't look the wizard in the eyes!", null);
////					vanstromNormalCount = 0; // reset normal attack counter after special is used
////			}
//
//			switch (animationID)
//			{
//				case 8704: // Vanstrom normal attack
//					vanstromNormalCount = vanstromNormalCount + 1;
//					client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Normal Attack Count: " + vanstromNormalCount, null);
//				case 8722: // Vanstrom special attack
//					vanstromSpecialCount = vanstromSpecialCount + 1;
//					vanstromNormalCount = 0;
//					animationTable.put(8704, 0);
//					client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Special Attack Count " + vanstromSpecialCount, null);
//				case -1:
//					client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", " npcName: " + npcName + " npcId: " + npcId + " npcLevel: " + npcLevel + " animationID: " + animationID, null);
//			}
//
//			if (vanstromNormalCount == 8){ // Prepare for special attack
//				client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "RUNAWAY LITTLE GIRL! RUNAWAY!", null);
//				customtrack = new MP3Player(getClass().getClassLoader().getResource("run_away_little_girl.mp3"));
//				customtrack.play();
//			}
//
//		}


		switch (animationID)
		{
			case 8704: // Vanstrom normal attack
				vanstromNormalCount = vanstromNormalCount + 1;
				client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Normal Attack count: " + vanstromNormalCount, null);
				break;
			case 8722: // Vanstrom special attack
				vanstromSpecialCount = vanstromSpecialCount + 1;
				vanstromNormalCount = 0;
				client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Special attack #  " + vanstromSpecialCount, null);
				break;
			case -1:
				client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", " npcName: " + npcName + " npcId: " + npcId + " npcLevel: " + npcLevel + " animationID: " + animationID, null);
				break;
		}

		if (vanstromNormalCount == 9){ // Prepare for special attack after 9 normal attacks
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "RUNAWAY LITTLE GIRL! RUNAWAY!", null);
			customtrack = new MP3Player(getClass().getClassLoader().getResource("run_away_little_girl.mp3"));
			customtrack.play();
		}


		// GUARD TEST LOGIC
//		switch (animationID)
//		{
//			case 386: // Guard attack with sword
//				guardSwordAttackCount = guardSwordAttackCount + 1;
//				client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Normal Attack count: " + guardSwordAttackCount, null);
//				//client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Guard (sword) attacks with sword !" + animationID, null);
//				break;
//			case 388: // Guard get attacked
//				guardSwordAttackCount = 0;
//				client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Guard (sword) gets attacked! Resetting Guard Sword count to 0 " + animationID, null);
//				break;
//			case 425:// Guard get attacked (bow)
//				//client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Guard (bow) gets attacked! " + animationID, null);
//			case 426: //Guard attacks with bow
//				//client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Guard (bow) attacks with bow! " + animationID, null);
//			case -1:
//				client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", " npcName: " + npcName + " npcId: " + npcId + " npcLevel: " + npcLevel + " animationID: " + animationID, null);
//				break;
//		}
//
//		if (guardSwordAttackCount == 9){ // Prepare for special attack
//				client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "RUNAWAY LITTLE GIRL! RUNAWAY!", null);
//				customtrack = new MP3Player(getClass().getClassLoader().getResource("run_away_little_girl.mp3"));
//				customtrack.play();
//		}

	}
	@Provides
	ExampleConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ExampleConfig.class);
	}
}
