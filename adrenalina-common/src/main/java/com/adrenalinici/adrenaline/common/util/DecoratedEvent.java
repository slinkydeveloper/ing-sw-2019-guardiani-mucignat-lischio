package com.adrenalinici.adrenaline.common.util;

public class DecoratedEvent<EVENT_TYPE, EVENT_SOURCE> {

  EVENT_TYPE innerEvent;
  EVENT_SOURCE eventSource;

  public DecoratedEvent(EVENT_TYPE innerEvent, EVENT_SOURCE eventSource) {
    this.innerEvent = innerEvent;
    this.eventSource = eventSource;
  }

  public EVENT_TYPE getInnerEvent() {
    return innerEvent;
  }

  public EVENT_SOURCE getEventSource() {
    return eventSource;
  }
}
