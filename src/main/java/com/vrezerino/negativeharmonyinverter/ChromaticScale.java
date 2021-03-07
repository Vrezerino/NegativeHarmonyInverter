package com.vrezerino.negativeharmonyinverter;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ChromaticScale {
    
    private final LinkedHashMap cs;
    private final ArrayList csAsList;
    
    public ChromaticScale() {
       /* 
        * This map represents the chromatic scale. Each note is paired with an entity that holds
        * two numbers.
        * 
        * The first number denotes the distance in half-steps, from note in question to desired
        * inverted note in the key of C (because we will use it as a basis for inverting notes
        * in other keys), i.e over the axis of C/G, which is in fact between Eb and E.
        * 
        * Second number is a half-step shift in the key of note in question, relative to the key
        * of C: it is added to first number during inversion.
        * 
        * For example, if you want to invert F over the axis of C/G, the
        * inverted note of F is (9 + 0) semitones up (in other words 3 semitones down) from F,
        * which is D.
        */
        this.cs = new LinkedHashMap();
        
        cs.put("C", new IntervalObject(7, 0)); cs.put("Db", new IntervalObject(5, 2));
        cs.put("D", new IntervalObject(3, 4)); cs.put("Eb", new IntervalObject(1, 6));
        cs.put("E", new IntervalObject(11, 8)); cs.put("F", new IntervalObject(9, 10));
        cs.put("Gb", new IntervalObject(7, 0)); cs.put("G", new IntervalObject(5, 2));
        cs.put("Ab", new IntervalObject(3, 4)); cs.put("A", new IntervalObject(1, 6));
        cs.put("Bb", new IntervalObject(11, 8)); cs.put("B", new IntervalObject(9, 10));
        
        this.csAsList = new ArrayList(this.cs.keySet());
    }
    
    public LinkedHashMap getScale() {
        return this.cs;
    }
        
    public String getNote(String note, int interval) {
       /*
        * This method returns a note from the chromatic scale a given number of half-steps 
        * away from provided note.
        */
        return this.csAsList.contains(note) ? 
                this.csAsList.get((this.csAsList.indexOf(note)+interval)%12).toString() : 
                " " ;
    }
}