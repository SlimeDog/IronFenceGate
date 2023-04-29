package com.github.justadeni.ironfencegate.misc;

import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.*;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.*;

public class LogFilter extends AbstractFilter {

    public LogFilter() {
        Logger logger = (Logger) LogManager.getRootLogger();
        logger.addFilter(this);
    }

    public Filter.Result checkMessage(final String message) {
        if (message == null)
            return Result.NEUTRAL;

        return message.contains("No data fixer registered for custom_") ? Result.DENY : Result.NEUTRAL;
    }

    public LifeCycle.State getState() {
        try {
            return LifeCycle.State.STARTED;
        }
        catch (Exception ex) {
            return null;
        }
    }

    @Override
    public Filter.Result filter(final LogEvent event) {
        if (event == null)
            return Result.NEUTRAL;

        return this.checkMessage(event.getMessage().getFormattedMessage());
    }

    @Override
    public Filter.Result filter(final Logger arg0, final Level arg1, final Marker arg2, final String message, final Object... arg4) {
        return this.checkMessage(message);
    }

    @Override
    public Filter.Result filter(final Logger arg0, final Level arg1, final Marker arg2, final String message, final Object arg4) {
        return this.checkMessage(message);
    }

    @Override
    public Filter.Result filter(final Logger arg0, final Level arg1, final Marker arg2, final Object message, final Throwable arg4) {
        return this.checkMessage(message.toString());
    }

    @Override
    public Filter.Result filter(final Logger arg0, final Level arg1, final Marker arg2, final Message message, final Throwable arg4) {
        return this.checkMessage(message.getFormattedMessage());
    }

    @Override
    public Filter.Result filter(final Logger arg0, final Level arg1, final Marker arg2, final String message, final Object arg4, final Object arg5) {
        return this.checkMessage(message);
    }

    @Override
    public Filter.Result filter(final Logger arg0, final Level arg1, final Marker arg2, final String message, final Object arg4, final Object arg5, final Object arg6) {
        return this.checkMessage(message);
    }

    @Override
    public Filter.Result filter(final Logger arg0, final Level arg1, final Marker arg2, final String message, final Object arg4, final Object arg5, final Object arg6, final Object arg7) {
        return this.checkMessage(message);
    }

    @Override
    public Filter.Result filter(final Logger arg0, final Level arg1, final Marker arg2, final String message, final Object arg4, final Object arg5, final Object arg6, final Object arg7, final Object arg8) {
        return this.checkMessage(message);
    }

    @Override
    public Filter.Result filter(final Logger arg0, final Level arg1, final Marker arg2, final String message, final Object arg4, final Object arg5, final Object arg6, final Object arg7, final Object arg8, final Object arg9) {
        return this.checkMessage(message);
    }

    @Override
    public Filter.Result filter(final Logger arg0, final Level arg1, final Marker arg2, final String message, final Object arg4, final Object arg5, final Object arg6, final Object arg7, final Object arg8, final Object arg9, final Object arg10) {
        return this.checkMessage(message);
    }

    @Override
    public Filter.Result filter(final Logger arg0, final Level arg1, final Marker arg2, final String message, final Object arg4, final Object arg5, final Object arg6, final Object arg7, final Object arg8, final Object arg9, final Object arg10, final Object arg11) {
        return this.checkMessage(message);
    }

    @Override
    public Filter.Result filter(final Logger arg0, final Level arg1, final Marker arg2, final String message, final Object arg4, final Object arg5, final Object arg6, final Object arg7, final Object arg8, final Object arg9, final Object arg10, final Object arg11, final Object arg12) {
        return this.checkMessage(message);
    }

    @Override
    public Filter.Result filter(final Logger arg0, final Level arg1, final Marker arg2, final String message, final Object arg4, final Object arg5, final Object arg6, final Object arg7, final Object arg8, final Object arg9, final Object arg10, final Object arg11, final Object arg12, final Object arg13) {
        return this.checkMessage(message);
    }
}
