package com.example;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;

import com.example.database.DatabaseHelper;
import com.example.model.Utilisateur;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 34)
public class ExampleRobolectricTest {

    private DatabaseHelper databaseHelper;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        databaseHelper = new DatabaseHelper(context);
        databaseHelper.viderTable();
    }

    @After
    public void tearDown() {
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }

    @Test
    public void testAjouterEtListerUtilisateurs() {
        boolean ajoute1 = databaseHelper.ajouterUtilisateur("Soa", 45);
        boolean ajoute2 = databaseHelper.ajouterUtilisateur("Alice", 30);

        assertTrue(ajoute1);
        assertTrue(ajoute2);

        List<Utilisateur> utilisateurs = databaseHelper.getTousLesUtilisateurs();
        assertEquals(2, utilisateurs.size());

        assertEquals("Soa", utilisateurs.get(0).getNom());
        assertEquals(45, utilisateurs.get(0).getAge());

        assertEquals("Alice", utilisateurs.get(1).getNom());
        assertEquals(30, utilisateurs.get(1).getAge());
    }

    @Test
    public void testSupprimerUtilisateur() {
        databaseHelper.ajouterUtilisateur("Soa", 45);
        List<Utilisateur> list = databaseHelper.getTousLesUtilisateurs();
        assertFalse(list.isEmpty());

        int id = list.get(0).getId();
        boolean supprime = databaseHelper.supprimerUtilisateur(id);
        assertTrue(supprime);

        List<Utilisateur> listApres = databaseHelper.getTousLesUtilisateurs();
        assertEquals(0, listApres.size());
    }
}
