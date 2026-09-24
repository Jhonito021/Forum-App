package com.example;

import com.example.model.Utilisateur;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExampleUnitTest {

    @Test
    public void testUtilisateurModel() {
        Utilisateur user = new Utilisateur(1, "Soa", 45);
        assertEquals(1, user.getId());
        assertEquals("Soa", user.getNom());
        assertEquals(45, user.getAge());
        assertEquals("Soa | 45 ans", user.toString());
    }
}
