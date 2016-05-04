package com.example.marvoot.testingandroid.Model;

/**
 * Created by Marvoot on 4/20/2016.
 */
public class CheckUserInteraction {
    final int InteractionType;

    public CheckUserInteraction(int interactionType) {
        this.InteractionType = interactionType;
    }

    public int GetInteractionType ()
    {
        return this.InteractionType;
    }
}
