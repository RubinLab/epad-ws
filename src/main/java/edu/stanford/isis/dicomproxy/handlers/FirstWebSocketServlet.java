/*
 * Copyright 2012 The Board of Trustees of the Leland Stanford Junior University.
 * Author: Daniel Rubin, Alan Snyder, Debra Willrett. All rights reserved. Possession
 * or use of this program is subject to the terms and conditions of the Academic
 * Software License Agreement available at:
 *   http://epad.stanford.edu/license/
 */
package edu.stanford.isis.dicomproxy.handlers;

//TODO AAA Had to comment out body to run with latest versions of jars
//TODO AAA Should this be deprecated
/**
 * First test of a WebSocket servlet - not being used (TBR).
 * 
 * <p>This does not seem to match the current version of Jetty</p>
 *
 * @author amsnyder
 */
public class FirstWebSocketServlet // extends WebSocketServlet
{
	/*
    private final Set<FirstWebSocket> _members = new CopyOnWriteArraySet<FirstWebSocket>();

    @Override
    protected WebSocket doWebSocketConnect(HttpServletRequest httpServletRequest, String s) {
        return new FirstWebSocket();
    }

    class FirstWebSocket implements WebSocket
    {
        Outbound _outbound;

        @Override
        public void onConnect(Outbound outbound) {
            _outbound=outbound;
            _members.add(this);
        }

        @Override
        public void onMessage(byte frame, String data) {
            for (FirstWebSocket member : _members)
            {
                try
                {
                    member._outbound.sendMessage(frame,data);
                }
                catch(IOException e) {
                    //Log.warn(e);
                }
            }
        }

        @Override
        public void onFragment(boolean bool, byte b, byte[] bytes, int i, int i1) {}

        @Override
        public void onMessage(byte frame, byte[] data, int offset, int length) {}

        @Override
        public void onDisconnect() {
            _members.remove(this);
        }
    }
*/
}
