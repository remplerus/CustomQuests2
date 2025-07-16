package com.vincentmet.customquests.events;

import net.minecraftforge.eventbus.api.Event;

public class DataLoadingEvent extends Event{
    public static class Pre extends DataLoadingEvent{}
    public static class Post extends DataLoadingEvent{}
}
