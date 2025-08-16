package com.vincentmet.customquests.gui.editor;

import com.vincentmet.customquests.Constants;
import com.vincentmet.customquests.api.ChapterHelper;
import com.vincentmet.customquests.api.ClientUtils;
import com.vincentmet.customquests.api.IHoverRenderable;
import com.vincentmet.customquests.api.QuestHelper;
import com.vincentmet.customquests.gui.EditorScreen;
import com.vincentmet.customquests.gui.EditorScreenManager;
import com.vincentmet.customquests.helpers.CQGuiEventListener;
import com.vincentmet.customquests.helpers.Container;
import com.vincentmet.customquests.helpers.IntCounter;
import com.vincentmet.customquests.helpers.math.Vec2i;
import com.vincentmet.customquests.helpers.rendering.VariableButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntSupplier;

public class EditorPropertiesSubScreen implements IHoverRenderable, CQGuiEventListener {
    private final EditorScreen parent;
    private final EditorScreenManager screenManager;
    private final IntSupplier x;
    private final IntSupplier y;
    private final IntSupplier width;
    private final IntSupplier height;
    private static final IntSupplier KEY_VALUE_HEIGHT = ()->24;
    private static final IntSupplier KEY_VALUE_SPACER = ()->5;
    private int scrollDistance = 0;
    private final List<KeyValueEntry> keyValueList = new ArrayList<>();
    private VariableButton removeChapterButton;
    private VariableButton removeQuestButton;
    private VariableButton removeTaskButton;

    private static final Component TRANSLATION_USE_SUBMENU = Component.translatable(Constants.MODID + ".editor.use_submenu");
    
    public EditorPropertiesSubScreen(EditorScreen parent, EditorScreenManager screenManager, IntSupplier x, IntSupplier y, IntSupplier width, IntSupplier height){
        this.parent = parent;
        this.screenManager = screenManager;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        reInit();
    }
    
    public void reInit(){
        removeChapterButton = new VariableButton(x, ()->0, ()->20, ()->18, VariableButton.ButtonTexture.DEFAULT_NORMAL, "-", new Vec2i(), mouseButton -> {
            ClientUtils.EditorMessages.Delete.requestDeleteChapter(screenManager.getSelectedChapterId());
            screenManager.setToParent();
            parent.actionQueue.push(parent::reInit);
            parent.actionQueue.push(parent::resetSelectorScroll);
        }, new ArrayList<>());
        removeQuestButton = new VariableButton(x, ()->0, ()->20, ()->18, VariableButton.ButtonTexture.DEFAULT_NORMAL, "-", new Vec2i(), mouseButton -> {
            ClientUtils.EditorMessages.Delete.requestDeleteQuest(screenManager.getSelectedQuestId());
            screenManager.setToParent();
            parent.actionQueue.push(parent::reInit);
            parent.actionQueue.push(parent::resetSelectorScroll);
        }, new ArrayList<>());
        removeTaskButton = new VariableButton(x, ()->0, ()->20, ()->18, VariableButton.ButtonTexture.DEFAULT_NORMAL, "-", new Vec2i(), mouseButton -> {
            ClientUtils.EditorMessages.Delete.requestDeleteTask(screenManager.getSelectedQuestId(), screenManager.getSelectedTaskId());
            screenManager.setToParent();
            parent.actionQueue.push(parent::reInit);
            parent.actionQueue.push(parent::resetSelectorScroll);
        }, new ArrayList<>());
        
        keyValueList.clear();
        List<IEditorEntry> editorEntries = new ArrayList<>();
        //todo continue this one over time
        editorEntries = switch (screenManager.getSelection()) {
            case CHAPTER -> ChapterHelper.getEditorChapterEntries(screenManager.getSelectedChapterId());
            case CHAPTER_TITLE -> ChapterHelper.getEditorChapterTitleEntries(screenManager.getSelectedChapterId());
            case CHAPTER_TEXT -> ChapterHelper.getEditorChapterTextEntries(screenManager.getSelectedChapterId());
            case CHAPTER_QUESTLIST ->
                    ChapterHelper.getEditorChapterQuestlistEntries(screenManager.getSelectedChapterId());
            case QUEST -> QuestHelper.getEditorQuestEntries(screenManager.getSelectedQuestId());
            case QUEST_BUTTON -> QuestHelper.getEditorQuestButtonEntries(screenManager.getSelectedQuestId());
            case QUEST_TITLE -> QuestHelper.getEditorQuestTitleEntries(screenManager.getSelectedQuestId());
            case QUEST_SUBTITLE -> QuestHelper.getEditorQuestSubtitleEntries(screenManager.getSelectedQuestId());
            case QUEST_TEXT -> QuestHelper.getEditorQuestTextEntries(screenManager.getSelectedQuestId());
            case QUEST_DEPENDENCIES ->
                    QuestHelper.getEditorQuestDependenciesEntries(screenManager.getSelectedQuestId());//todo allow for add/remove quest ids
            case QUEST_TASKS -> QuestHelper.getEditorQuestTasksEntries(screenManager.getSelectedQuestId());
            case QUEST_TASK ->
                    QuestHelper.getEditorQuestTaskEntries(screenManager.getSelectedQuestId(), screenManager.getSelectedTaskId());
            case QUEST_TASK_SUBTASKS ->
                    QuestHelper.getEditorQuestSubtasksEntries(screenManager.getSelectedQuestId(), screenManager.getSelectedTaskId());
            case QUEST_TASK_SUBTASK ->
                    QuestHelper.getEditorQuestSubtaskEntries(screenManager.getSelectedQuestId(), screenManager.getSelectedTaskId(), screenManager.getSelectedTaskId());
            case QUEST_POSITION -> QuestHelper.getEditorQuestPositionEntries(screenManager.getSelectedQuestId());
            default -> editorEntries;
        };
        IntCounter cumulativeHeight = new IntCounter(y.getAsInt() + KEY_VALUE_SPACER.getAsInt(), KEY_VALUE_HEIGHT.getAsInt() + KEY_VALUE_SPACER.getAsInt());
        editorEntries.forEach(iEditorEntry -> {
            Container<Integer> ySupplier = new Container<>(cumulativeHeight.getValue());
            keyValueList.add(new KeyValueEntry(screenManager, iEditorEntry, x, ySupplier::get, width, KEY_VALUE_HEIGHT));
            cumulativeHeight.count();
        });
        applyScrollLimits();
    }

    @Override
    public void render(GuiGraphics matrixStack, int mouseX, int mouseY, float partialTicks){
        keyValueList.forEach(keyValueEntry -> keyValueEntry.render(matrixStack, mouseX, mouseY, partialTicks));
        if(screenManager.getSelection() == MenuSelection.CHAPTER){
            removeChapterButton.render(matrixStack, mouseX, mouseY, partialTicks);
        }else if(screenManager.getSelection() == MenuSelection.QUEST){
            removeQuestButton.render(matrixStack, mouseX, mouseY, partialTicks);
        }else if(screenManager.getSelection() == MenuSelection.QUEST_TASK){
            removeTaskButton.render(matrixStack, mouseX, mouseY, partialTicks);
        }
        if(keyValueList.isEmpty()){
            matrixStack.drawString(Minecraft.getInstance().font, TRANSLATION_USE_SUBMENU, x.getAsInt()+5, y.getAsInt()+5, 0xFFFFFF);
        }
    }
    
    @Override
    public void renderHover(GuiGraphics matrixStack, int mouseX, int mouseY, float partialTicks){
        keyValueList.forEach(keyValueEntry -> keyValueEntry.renderHover(matrixStack, mouseX, mouseY, partialTicks));
        if(screenManager.getSelection() == MenuSelection.CHAPTER){
            removeChapterButton.renderHover(matrixStack, mouseX, mouseY, partialTicks);
        }else if(screenManager.getSelection() == MenuSelection.QUEST){
            removeQuestButton.renderHover(matrixStack, mouseX, mouseY, partialTicks);
        }else if(screenManager.getSelection() == MenuSelection.QUEST_TASK){
            removeTaskButton.renderHover(matrixStack, mouseX, mouseY, partialTicks);
        }
    }
    
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers){
        keyValueList.forEach(keyValueEntry -> keyValueEntry.keyPressed(keyCode, scanCode, modifiers));
        if(screenManager.getSelection() == MenuSelection.CHAPTER){
            removeChapterButton.keyPressed(keyCode, scanCode, modifiers);
        }else if(screenManager.getSelection() == MenuSelection.QUEST){
            removeQuestButton.keyPressed(keyCode, scanCode, modifiers);
        }else if(screenManager.getSelection() == MenuSelection.QUEST_TASK){
            removeTaskButton.keyPressed(keyCode, scanCode, modifiers);
        }
        return false;
    }
    
    @Override
    public void mouseMoved(double mouseX, double mouseY){
        keyValueList.forEach(keyValueEntry -> keyValueEntry.mouseMoved(mouseX, mouseY));
        if(screenManager.getSelection() == MenuSelection.CHAPTER){
            removeChapterButton.mouseMoved(mouseX, mouseY);
        }else if(screenManager.getSelection() == MenuSelection.QUEST){
            removeQuestButton.mouseMoved(mouseX, mouseY);
        }else if (screenManager.getSelection() == MenuSelection.QUEST_TASK){
            removeTaskButton.mouseMoved(mouseX, mouseY);
        }
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button){
        keyValueList.forEach(keyValueEntry -> keyValueEntry.mouseClicked(mouseX, mouseY, button));
        if(screenManager.getSelection() == MenuSelection.CHAPTER){
            removeChapterButton.mouseClicked(mouseX, mouseY, button);
        }else if(screenManager.getSelection() == MenuSelection.QUEST){
            removeQuestButton.mouseClicked(mouseX, mouseY, button);
        }else if (screenManager.getSelection() == MenuSelection.QUEST_TASK){
            removeTaskButton.mouseClicked(mouseX, mouseY, button);
        }
        return false;
    }
    
    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button){
        keyValueList.forEach(keyValueEntry -> keyValueEntry.mouseReleased(mouseX, mouseY, button));
        if(screenManager.getSelection() == MenuSelection.CHAPTER){
            removeChapterButton.mouseReleased(mouseX, mouseY, button);
        }else if(screenManager.getSelection() == MenuSelection.QUEST){
            removeQuestButton.mouseReleased(mouseX, mouseY, button);
        }else if (screenManager.getSelection() == MenuSelection.QUEST_TASK){
            removeTaskButton.mouseReleased(mouseX, mouseY, button);
        }
        return false;
    }
    
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY){
        keyValueList.forEach(keyValueEntry -> keyValueEntry.mouseDragged(mouseX, mouseY, button, dragX, dragY));
        if(screenManager.getSelection() == MenuSelection.CHAPTER){
            removeChapterButton.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }else if(screenManager.getSelection() == MenuSelection.QUEST){
            removeQuestButton.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }else if (screenManager.getSelection() == MenuSelection.QUEST_TASK){
            removeTaskButton.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }
        return false;
    }
    
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta){
        keyValueList.forEach(keyValueEntry -> keyValueEntry.mouseScrolled(mouseX, mouseY, delta));
        if(screenManager.getSelection() == MenuSelection.CHAPTER){
            removeChapterButton.mouseScrolled(mouseX, mouseY, delta);
        }else if(screenManager.getSelection() == MenuSelection.QUEST){
            removeQuestButton.mouseScrolled(mouseX, mouseY, delta);
        }else if (screenManager.getSelection() == MenuSelection.QUEST_TASK){
            removeTaskButton.mouseScrolled(mouseX, mouseY, delta);
        }
        return false;
    }
    
    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers){
        keyValueList.forEach(keyValueEntry -> keyValueEntry.keyReleased(keyCode, scanCode, modifiers));
        if(screenManager.getSelection() == MenuSelection.CHAPTER){
            removeChapterButton.keyReleased(keyCode, scanCode, modifiers);
        }else if(screenManager.getSelection() == MenuSelection.QUEST){
            removeQuestButton.keyReleased(keyCode, scanCode, modifiers);
        }else if (screenManager.getSelection() == MenuSelection.QUEST_TASK){
            removeTaskButton.keyReleased(keyCode, scanCode, modifiers);
        }
        return false;
    }
    
    @Override
    public boolean charTyped(char codePoint, int modifiers){
        keyValueList.forEach(keyValueEntry -> keyValueEntry.charTyped(codePoint, modifiers));
        if(screenManager.getSelection() == MenuSelection.CHAPTER){
            removeChapterButton.charTyped(codePoint, modifiers);
        }else if(screenManager.getSelection() == MenuSelection.QUEST){
            removeQuestButton.charTyped(codePoint, modifiers);
        }else if (screenManager.getSelection() == MenuSelection.QUEST_TASK){
            removeTaskButton.charTyped(codePoint, modifiers);
        }
        return false;
    }
    
    @Override
    public boolean changeFocus(boolean focus){
        keyValueList.forEach(keyValueEntry -> keyValueEntry.changeFocus(focus));
        if(screenManager.getSelection() == MenuSelection.CHAPTER){
            removeChapterButton.changeFocus(focus);
        }else if(screenManager.getSelection() == MenuSelection.QUEST){
            removeQuestButton.changeFocus(focus);
        }else if (screenManager.getSelection() == MenuSelection.QUEST_TASK){
            removeTaskButton.changeFocus(focus);
        }
        return false;
    }
    
    @Override
    public boolean isMouseOver(double mouseX, double mouseY){
        keyValueList.forEach(keyValueEntry -> keyValueEntry.isMouseOver(mouseX, mouseY));
        if(screenManager.getSelection() == MenuSelection.CHAPTER){
            removeChapterButton.isMouseOver(mouseX, mouseY);
        }else if(screenManager.getSelection() == MenuSelection.QUEST){
            removeQuestButton.isMouseOver(mouseX, mouseY);
        }else if (screenManager.getSelection() == MenuSelection.QUEST_TASK){
            removeTaskButton.isMouseOver(mouseX, mouseY);
        }
        return false;
    }
    
    private void applyScrollLimits(){
        if(this.scrollDistance < 0){
            this.scrollDistance = 0;
        }
        
        if(this.scrollDistance > getMaxScroll()){
            this.scrollDistance = getMaxScroll();
        }
    }
    
    private int getMaxScroll(){
        return Math.max(this.getContentHeight() - height.getAsInt(), 0);
    }
    
    private int getContentHeight(){
        //todo maybe a switch here, some somehow make a getter in the classes for this one, OR calculate by the amount of entries from the already existing getter + array_length-1 when applicable
        /*if(screenManager.getCurrentlySelectedQuestId()>=0){
            return FONT.trimStringToWidth(new StringTextComponent(TextUtils.colorify(QuestingStorage.getSidedQuestsMap().get(screenManager.getCurrentlySelectedQuestId()).getStyledText().getStyledText())), textContentWidth.getAsInt()).size() * (FONT.FONT_HEIGHT + NEWLINE_MARGIN);
        }*/
        return 0;
    }
}