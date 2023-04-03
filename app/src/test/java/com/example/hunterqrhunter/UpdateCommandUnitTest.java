package com.example.hunterqrhunter;

import com.example.hunterqrhunter.data.UpdateCommand;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class UpdateCommandUnitTest {
    @Test
    public void testUpdateCommandConstruction() {
        FirebaseFirestore firestoreMock = mock(FirebaseFirestore.class);
        UpdateCommand updateCommand = null;
        updateCommand = new UpdateCommand(firestoreMock);
        assertNotNull(updateCommand);
    }
}
