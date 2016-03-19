package module6;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.MapUtils;
import parsing.ParseFeed;
import processing.core.PApplet;

import java.util.HashMap;
import java.util.List;

import de.fhpotsdam.unfolding.marker.Marker;

/**
 * Visualizes life expectancy in different countries.
 *
 * It loads the country shapes from a GeoJSON file via a data reader, and loads the population density values from
 * another CSV file (provided by the World Bank). The data value is encoded to transparency via a simplistic linear
 * mapping.
 */
public class Gdp2014 extends PApplet {

    UnfoldingMap map;
    HashMap<String, Float> lifeExpMap;
    List<Feature> countries;
    List<Marker> countryMarkers;

    public void setup() {
        size(1300, 800, OPENGL);
        map = new UnfoldingMap(this, 300, 50, 1000, 750, new Google.GoogleMapProvider());

        // allow map to be interactive
        MapUtils.createDefaultEventDispatcher(this, map);

        // Load lifeExpectancy data
        lifeExpMap = ParseFeed.loadGdpFromCSV(this,"C:\\Users\\Kai\\Desktop\\idea\\UCSDUnfoldingMaps\\data\\gdp.csv");


        // Load country polygons and adds them as markers
        countries = GeoJSONReader.loadData(this, "C:\\Users\\Kai\\Desktop\\idea\\UCSDUnfoldingMaps\\data\\countries.geo.json");
        countryMarkers = MapUtils.createSimpleMarkers(countries);
        map.addMarkers(countryMarkers);
        System.out.println(countryMarkers.get(0).getId());

        // Country markers are shaded according to life expectancy (only once)
        shadeCountries();
    }

    public void draw() {
        // Draw map tiles and country markers
        map.draw();
        addKey();
    }

    //Helper method to color each country based on life expectancy
    //Red-orange indicates low (near 40)
    //Blue indicates high (near 100)
    private void shadeCountries() {
        for (Marker marker : countryMarkers) {
            // Find data for country of the current marker
            String countryId = marker.getId();
            System.out.println(lifeExpMap.containsKey(countryId));
            if (lifeExpMap.containsKey(countryId)) {
                float lifeExp = (lifeExpMap.get(countryId))/1000000000;
                // Encode value as brightness (values range: 40-90, assign color 10-255)
                int colorLevel = (int) map(lifeExp, 50, 7000, 0, 255);
                marker.setColor(color(colorLevel, 130, 255-colorLevel));
            }
            else {
                marker.setColor(color(150,150,150));
            }
        }
    }

    // helper method to draw key in GUI
    private void addKey() {
        // Remember you can use Processing's graphics methods here
        fill(255, 250, 240);

        int xbase = 25;
        int ybase = 50;

        rect(xbase, ybase, 305, 395);

        fill(0);
        textAlign(LEFT, CENTER);
        textSize(16);
        text("World PPP GDP (2014)", xbase+25, ybase+25);

        String s ="PPP GDP is gross domestic product converted to international dollars using purchasing power parity rates. An international dollar has the same purchasing power over GDP as the U.S. dollar has in the United States. GDP is the sum of gross value added by all resident producers in the economy plus any product taxes and minus any subsidies not included in the value of the products.";
        textSize(12);
        text(s, xbase+25, ybase+55, 260, 210);

        int blue = color(0, 130, 255);
        int red = color(255, 130, 0);
        //background(0);
        gradientRect(50, 325, 33, 100, red, blue);

        fill(0);
        textAlign(LEFT, CENTER);
        textSize(12);
        text(">7 trillion USD", 90, 325);

        fill(0);
        textAlign(LEFT, CENTER);
        textSize(12);
        text("<5 billion USD", 90, 415);

    }

    void gradientRect(int x, int y, int w, int h, int c1, int c2) {
        beginShape();
        fill(c1);
        vertex(x,y);
        vertex(x+w,y);
        //vertex(x,y+h);
        fill(c2);
        vertex(x+w,y+h);
        vertex(x,y+h);
        //vertex(x+w,y);
        endShape();
    }



}
