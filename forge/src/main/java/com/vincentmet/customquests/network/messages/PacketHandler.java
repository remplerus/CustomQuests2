package com.vincentmet.customquests.network.messages;

import com.vincentmet.customquests.Constants;
import com.vincentmet.customquests.network.messages.button.MessageRewardClaim;
import com.vincentmet.customquests.network.messages.button.MessageTaskButton;
import com.vincentmet.customquests.network.messages.command.MessageDiscord;
import com.vincentmet.customquests.network.messages.command.MessageHand;
import com.vincentmet.customquests.network.messages.command.MessageOpenEditor;
import com.vincentmet.customquests.network.messages.editor.cts.requests.create.*;
import com.vincentmet.customquests.network.messages.editor.cts.requests.delete.*;
import com.vincentmet.customquests.network.messages.editor.cts.requests.update.chapter.*;
import com.vincentmet.customquests.network.messages.editor.cts.requests.update.quest.*;
import com.vincentmet.customquests.network.messages.sync.MessageUpdateDelivery;
import com.vincentmet.customquests.network.messages.sync.MessageUpdateServerSettings;
import com.vincentmet.customquests.network.messages.sync.MessageUpdateSinglePlayer;
import com.vincentmet.customquests.network.messages.sync.MessageUpdateSinglePlayerQuestProgress;
import com.vincentmet.customquests.network.messages.sync.stc.clear.*;
import com.vincentmet.customquests.network.messages.sync.stc.delete.*;
import com.vincentmet.customquests.network.messages.sync.stc.update.*;
import com.vincentmet.customquests.standardcontent.messages.MessageCheckboxClick;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler{
	private static int messageID = 0;
	private static final String PROTOCOL_VERSION = Integer.toString(1);
	
	public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
			.named(new ResourceLocation(Constants.MODID, "network"))
			.clientAcceptedVersions(PROTOCOL_VERSION::equals)
			.serverAcceptedVersions(PROTOCOL_VERSION::equals)
			.networkProtocolVersion(() -> PROTOCOL_VERSION)
			.simpleChannel();
	
	private static int nextID() {
		return messageID++;
	}

	public static <T extends ICQPacket> void registerPacket(T packet, NetworkDirection networkDirection){
		PacketHandler.CHANNEL.messageBuilder(packet.getClass(), nextID(), networkDirection)
				.encoder(packet::encode)
				.decoder(packet::decode)
				.consumerMainThread(packet::handle)
				.add();
	}
	
	public static void init() {//todo add A LOT A LOT A LOT of new packets here
		//Main
		//button
		registerPacket(new MessageRewardClaim(), NetworkDirection.PLAY_TO_SERVER);
		registerPacket(new MessageTaskButton(), NetworkDirection.PLAY_TO_SERVER);
		//command
		registerPacket(new MessageDiscord(), NetworkDirection.PLAY_TO_CLIENT);
		registerPacket(new MessageHand(), NetworkDirection.PLAY_TO_CLIENT);
		registerPacket(new MessageOpenEditor(), NetworkDirection.PLAY_TO_CLIENT);
		//editor
		//editor/cts
		//editor/cts/requests
		//editor/cts/requests/create
		CHANNEL.registerMessage(nextID(), MessageEditorRequestCreateChapter.class, MessageEditorRequestCreateChapter::encode, MessageEditorRequestCreateChapter::decode, MessageEditorRequestCreateChapter::handle);
		CHANNEL.registerMessage(nextID(), MessageEditorRequestCreateQuest.class, MessageEditorRequestCreateQuest::encode, MessageEditorRequestCreateQuest::decode, MessageEditorRequestCreateQuest::handle);
		CHANNEL.registerMessage(nextID(), MessageEditorRequestCreateReward.class, MessageEditorRequestCreateReward::encode, MessageEditorRequestCreateReward::decode, MessageEditorRequestCreateReward::handle);
		CHANNEL.registerMessage(nextID(), MessageEditorRequestCreateSubreward.class, MessageEditorRequestCreateSubreward::encode, MessageEditorRequestCreateSubreward::decode, MessageEditorRequestCreateSubreward::handle);
		CHANNEL.registerMessage(nextID(), MessageEditorRequestCreateSubtask.class, MessageEditorRequestCreateSubtask::encode, MessageEditorRequestCreateSubtask::decode, MessageEditorRequestCreateSubtask::handle);
		CHANNEL.registerMessage(nextID(), MessageEditorRequestCreateTask.class, MessageEditorRequestCreateTask::encode, MessageEditorRequestCreateTask::decode, MessageEditorRequestCreateTask::handle);
		//editor/cts/requests/delete
		CHANNEL.registerMessage(nextID(), MessageEditorRequestDeleteChapter.class, MessageEditorRequestDeleteChapter::encode, MessageEditorRequestDeleteChapter::decode, MessageEditorRequestDeleteChapter::handle);
		CHANNEL.registerMessage(nextID(), MessageEditorRequestDeleteQuest.class, MessageEditorRequestDeleteQuest::encode, MessageEditorRequestDeleteQuest::decode, MessageEditorRequestDeleteQuest::handle);
		CHANNEL.registerMessage(nextID(), MessageEditorRequestDeleteReward.class, MessageEditorRequestDeleteReward::encode, MessageEditorRequestDeleteReward::decode, MessageEditorRequestDeleteReward::handle);
		CHANNEL.registerMessage(nextID(), MessageEditorRequestDeleteSubreward.class, MessageEditorRequestDeleteSubreward::encode, MessageEditorRequestDeleteSubreward::decode, MessageEditorRequestDeleteSubreward::handle);
		CHANNEL.registerMessage(nextID(), MessageEditorRequestDeleteSubtask.class, MessageEditorRequestDeleteSubtask::encode, MessageEditorRequestDeleteSubtask::decode, MessageEditorRequestDeleteSubtask::handle);
		CHANNEL.registerMessage(nextID(), MessageEditorRequestDeleteTask.class, MessageEditorRequestDeleteTask::encode, MessageEditorRequestDeleteTask::decode, MessageEditorRequestDeleteTask::handle);
		//editor/cts/requests/update
		//editor/cts/requests/update/chapter
		CHANNEL.registerMessage(nextID(), MessageEditorRequestChapterQuestlistAddQuestId.class, MessageEditorRequestChapterQuestlistAddQuestId::encode, MessageEditorRequestChapterQuestlistAddQuestId::decode, MessageEditorRequestChapterQuestlistAddQuestId::handle);
		CHANNEL.registerMessage(nextID(), MessageEditorRequestChapterQuestlistRemoveQuestId.class, MessageEditorRequestChapterQuestlistRemoveQuestId::encode, MessageEditorRequestChapterQuestlistRemoveQuestId::decode, MessageEditorRequestChapterQuestlistRemoveQuestId::handle);
		CHANNEL.registerMessage(nextID(), MessageEditorRequestUpdateChapterIcon.class, MessageEditorRequestUpdateChapterIcon::encode, MessageEditorRequestUpdateChapterIcon::decode, MessageEditorRequestUpdateChapterIcon::handle);
		CHANNEL.registerMessage(nextID(), MessageEditorRequestUpdateChapterTextText.class, MessageEditorRequestUpdateChapterTextText::encode, MessageEditorRequestUpdateChapterTextText::decode, MessageEditorRequestUpdateChapterTextText::handle);
		CHANNEL.registerMessage(nextID(), MessageEditorRequestUpdateChapterTextType.class, MessageEditorRequestUpdateChapterTextType::encode, MessageEditorRequestUpdateChapterTextType::decode, MessageEditorRequestUpdateChapterTextType::handle);
		CHANNEL.registerMessage(nextID(), MessageEditorRequestUpdateChapterTitleText.class, MessageEditorRequestUpdateChapterTitleText::encode, MessageEditorRequestUpdateChapterTitleText::decode, MessageEditorRequestUpdateChapterTitleText::handle);
		CHANNEL.registerMessage(nextID(), MessageEditorRequestUpdateChapterTitleType.class, MessageEditorRequestUpdateChapterTitleType::encode, MessageEditorRequestUpdateChapterTitleType::decode, MessageEditorRequestUpdateChapterTitleType::handle);
		//editor/cts/requests/update/quest
		CHANNEL.registerMessage(nextID(), MessageEditorRequestUpdateQuestButtonIcon.class, MessageEditorRequestUpdateQuestButtonIcon::encode, MessageEditorRequestUpdateQuestButtonIcon::decode, MessageEditorRequestUpdateQuestButtonIcon::handle);
		CHANNEL.registerMessage(nextID(), MessageEditorRequestUpdateQuestButtonScale.class, MessageEditorRequestUpdateQuestButtonScale::encode, MessageEditorRequestUpdateQuestButtonScale::decode, MessageEditorRequestUpdateQuestButtonScale::handle);
		CHANNEL.registerMessage(nextID(), MessageEditorRequestUpdateQuestButtonShape.class, MessageEditorRequestUpdateQuestButtonShape::encode, MessageEditorRequestUpdateQuestButtonShape::decode, MessageEditorRequestUpdateQuestButtonShape::handle);
		CHANNEL.registerMessage(nextID(), MessageEditorRequestUpdateQuestDependenciesAddQuestId.class, MessageEditorRequestUpdateQuestDependenciesAddQuestId::encode, MessageEditorRequestUpdateQuestDependenciesAddQuestId::decode, MessageEditorRequestUpdateQuestDependenciesAddQuestId::handle);
		CHANNEL.registerMessage(nextID(), MessageEditorRequestUpdateQuestDependenciesLogic.class, MessageEditorRequestUpdateQuestDependenciesLogic::encode, MessageEditorRequestUpdateQuestDependenciesLogic::decode, MessageEditorRequestUpdateQuestDependenciesLogic::handle);
		CHANNEL.registerMessage(nextID(), MessageEditorRequestUpdateQuestDependenciesRemoveQuestId.class, MessageEditorRequestUpdateQuestDependenciesRemoveQuestId::encode, MessageEditorRequestUpdateQuestDependenciesRemoveQuestId::decode, MessageEditorRequestUpdateQuestDependenciesRemoveQuestId::handle);
		CHANNEL.registerMessage(nextID(), MessageEditorRequestUpdateQuestPositionX.class, MessageEditorRequestUpdateQuestPositionX::encode, MessageEditorRequestUpdateQuestPositionX::decode, MessageEditorRequestUpdateQuestPositionX::handle);
		CHANNEL.registerMessage(nextID(), MessageEditorRequestUpdateQuestPositionY.class, MessageEditorRequestUpdateQuestPositionY::encode, MessageEditorRequestUpdateQuestPositionY::decode, MessageEditorRequestUpdateQuestPositionY::handle);
		CHANNEL.registerMessage(nextID(), MessageEditorRequestUpdateQuestRewardsLogic.class, MessageEditorRequestUpdateQuestRewardsLogic::encode, MessageEditorRequestUpdateQuestRewardsLogic::decode, MessageEditorRequestUpdateQuestRewardsLogic::handle);
		CHANNEL.registerMessage(nextID(), MessageEditorRequestUpdateQuestSubrewardContent.class, MessageEditorRequestUpdateQuestSubrewardContent::encode, MessageEditorRequestUpdateQuestSubrewardContent::decode, MessageEditorRequestUpdateQuestSubrewardContent::handle);
		CHANNEL.registerMessage(nextID(), MessageEditorRequestUpdateQuestSubrewardType.class, MessageEditorRequestUpdateQuestSubrewardType::encode, MessageEditorRequestUpdateQuestSubrewardType::decode, MessageEditorRequestUpdateQuestSubrewardType::handle);
		CHANNEL.registerMessage(nextID(), MessageEditorRequestUpdateQuestSubtaskContent.class, MessageEditorRequestUpdateQuestSubtaskContent::encode, MessageEditorRequestUpdateQuestSubtaskContent::decode, MessageEditorRequestUpdateQuestSubtaskContent::handle);
		CHANNEL.registerMessage(nextID(), MessageEditorRequestUpdateQuestSubtasksLogic.class, MessageEditorRequestUpdateQuestSubtasksLogic::encode, MessageEditorRequestUpdateQuestSubtasksLogic::decode, MessageEditorRequestUpdateQuestSubtasksLogic::handle);
		CHANNEL.registerMessage(nextID(), MessageEditorRequestUpdateQuestSubtitleText.class, MessageEditorRequestUpdateQuestSubtitleText::encode, MessageEditorRequestUpdateQuestSubtitleText::decode, MessageEditorRequestUpdateQuestSubtitleText::handle);
		CHANNEL.registerMessage(nextID(), MessageEditorRequestUpdateQuestSubtitleType.class, MessageEditorRequestUpdateQuestSubtitleType::encode, MessageEditorRequestUpdateQuestSubtitleType::decode, MessageEditorRequestUpdateQuestSubtitleType::handle);
		CHANNEL.registerMessage(nextID(), MessageEditorRequestUpdateQuestTasksLogic.class, MessageEditorRequestUpdateQuestTasksLogic::encode, MessageEditorRequestUpdateQuestTasksLogic::decode, MessageEditorRequestUpdateQuestTasksLogic::handle);
		CHANNEL.registerMessage(nextID(), MessageEditorRequestUpdateQuestTaskType.class, MessageEditorRequestUpdateQuestTaskType::encode, MessageEditorRequestUpdateQuestTaskType::decode, MessageEditorRequestUpdateQuestTaskType::handle);
		CHANNEL.registerMessage(nextID(), MessageEditorRequestUpdateQuestTextText.class, MessageEditorRequestUpdateQuestTextText::encode, MessageEditorRequestUpdateQuestTextText::decode, MessageEditorRequestUpdateQuestTextText::handle);
		CHANNEL.registerMessage(nextID(), MessageEditorRequestUpdateQuestTextType.class, MessageEditorRequestUpdateQuestTextType::encode, MessageEditorRequestUpdateQuestTextType::decode, MessageEditorRequestUpdateQuestTextType::handle);
		CHANNEL.registerMessage(nextID(), MessageEditorRequestUpdateQuestTitleText.class, MessageEditorRequestUpdateQuestTitleText::encode, MessageEditorRequestUpdateQuestTitleText::decode, MessageEditorRequestUpdateQuestTitleText::handle);
		CHANNEL.registerMessage(nextID(), MessageEditorRequestUpdateQuestTitleType.class, MessageEditorRequestUpdateQuestTitleType::encode, MessageEditorRequestUpdateQuestTitleType::decode, MessageEditorRequestUpdateQuestTitleType::handle);
		//sync
		registerPacket(new MessageUpdateDelivery(), NetworkDirection.PLAY_TO_SERVER);
		registerPacket(new MessageUpdateServerSettings(), NetworkDirection.PLAY_TO_CLIENT);
		//sync/stc
		//sync/stc/clear
		registerPacket(new MessageStcSyncTempClearAllChapters(), NetworkDirection.PLAY_TO_CLIENT);
		registerPacket(new MessageStcSyncTempClearAllParties(), NetworkDirection.PLAY_TO_CLIENT);
		registerPacket(new MessageStcSyncTempClearAllPlayers(), NetworkDirection.PLAY_TO_CLIENT);
		registerPacket(new MessageStcSyncTempClearAllQuests(), NetworkDirection.PLAY_TO_CLIENT);
		registerPacket(new MessageStcSyncTempClearSingleQuest(), NetworkDirection.PLAY_TO_CLIENT);
		registerPacket(new MessageStcSyncTempClearSingleReward(), NetworkDirection.PLAY_TO_CLIENT);
		registerPacket(new MessageStcSyncTempClearSingleSubreward(), NetworkDirection.PLAY_TO_CLIENT);
		registerPacket(new MessageStcSyncTempClearSingleSubtask(), NetworkDirection.PLAY_TO_CLIENT);
		registerPacket(new MessageStcSyncTempClearSingleTask(), NetworkDirection.PLAY_TO_CLIENT);
		//sync/stc/delete
		registerPacket(new MessageStcSyncDeleteAllChapters(), NetworkDirection.PLAY_TO_CLIENT);
		registerPacket(new MessageStcSyncDeleteAllQuests(), NetworkDirection.PLAY_TO_CLIENT);
		registerPacket(new MessageStcSyncDeleteSingleChapter(), NetworkDirection.PLAY_TO_CLIENT);
		registerPacket(new MessageStcSyncDeleteSingleParty(), NetworkDirection.PLAY_TO_CLIENT);
		registerPacket(new MessageStcSyncDeleteSinglePlayer(), NetworkDirection.PLAY_TO_CLIENT);
		registerPacket(new MessageStcSyncDeleteSingleQuest(), NetworkDirection.PLAY_TO_CLIENT);
		registerPacket(new MessageStcSyncDeleteSingleReward(), NetworkDirection.PLAY_TO_CLIENT);
		registerPacket(new MessageStcSyncDeleteSingleSubreward(), NetworkDirection.PLAY_TO_CLIENT);
		registerPacket(new MessageStcSyncDeleteSingleSubtask(), NetworkDirection.PLAY_TO_CLIENT);
		registerPacket(new MessageStcSyncDeleteSingleTask(), NetworkDirection.PLAY_TO_CLIENT);
		//sync/stc/update
		registerPacket(new MessageStcSyncUpdateSingleChapter(), NetworkDirection.PLAY_TO_CLIENT);
		registerPacket(new MessageStcSyncUpdateSingleParty(), NetworkDirection.PLAY_TO_CLIENT);
		registerPacket(new MessageStcSyncUpdateSinglePlayer(), NetworkDirection.PLAY_TO_CLIENT);
		registerPacket(new MessageStcSyncUpdateSingleQuest(), NetworkDirection.PLAY_TO_CLIENT);
		registerPacket(new MessageStcSyncUpdateSingleReward(), NetworkDirection.PLAY_TO_CLIENT);
		registerPacket(new MessageStcSyncUpdateSingleSubreward(), NetworkDirection.PLAY_TO_CLIENT);
		registerPacket(new MessageStcSyncUpdateSingleSubtask(), NetworkDirection.PLAY_TO_CLIENT);
		registerPacket(new MessageStcSyncUpdateSingleTask(), NetworkDirection.PLAY_TO_CLIENT);

		registerPacket(new MessageUpdateSinglePlayer(), NetworkDirection.PLAY_TO_CLIENT);
		registerPacket(new MessageUpdateSinglePlayerQuestProgress(), NetworkDirection.PLAY_TO_CLIENT);
		registerPacket(new MessageReinitQuestingCanvas(), NetworkDirection.PLAY_TO_CLIENT);

		//Standard Content
		registerPacket(new MessageCheckboxClick(), NetworkDirection.PLAY_TO_SERVER);
	}
}