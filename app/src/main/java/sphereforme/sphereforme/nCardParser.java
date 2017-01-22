package sphereforme.sphereforme;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by smfields on 1/21/17.
 */

public class nCardParser {

    public nCard getnCard(File f) throws ParserException {
        nCard ncard = new nCard();

        StringBuilder header = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;

            while ((line = br.readLine()) != null && !line.equals("#CARD#")) {
                header.append(line);
                header.append("\n");
            }

            ncard.setHeader(header.toString());

            //Begin reading file
            while ((line = br.readLine()) != null && !line.equals("#CARD#")) {
                String attr[] = line.split(": ");
                if(attr.length != 2) {
                    throw new ParserException();
                }
                ncard.addAttr(attr[0], attr[1]);

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ncard; //STUB
    }

}
