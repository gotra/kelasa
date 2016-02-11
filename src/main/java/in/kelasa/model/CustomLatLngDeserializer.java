package in.kelasa.model;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.google.maps.model.LatLng;


import java.io.IOException;

/**
 * Created by rajeevguru on 30/11/15.
 */
public class CustomLatLngDeserializer extends JsonDeserializer<LatLng> {


        public CustomLatLngDeserializer() {
        }

        public LatLng deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if(jp.isExpectedStartArrayToken()) {
                return this.deserializeArray(jp, ctxt);
            } else {
                throw ctxt.mappingException(LatLng.class);
            }
        }

        protected LatLng deserializeArray(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            LatLng node = new LatLng(this.extractDouble(jp, ctxt, false),this.extractDouble(jp, ctxt, false));

            if(jp.hasCurrentToken() && jp.getCurrentToken() != JsonToken.END_ARRAY) {
                jp.nextToken();
            }

            return node;
        }

    private double extractDouble(JsonParser jp, DeserializationContext ctxt, boolean optional)
            throws JsonParseException, IOException {
        JsonToken token = jp.nextToken();
        if (token == null) {
            if (optional)
                return Double.NaN;
            else
                throw ctxt.mappingException("Unexpected end-of-input when binding data into LatLng");
        }
        else {
            switch (token) {
                case END_ARRAY:
                    if (optional)
                        return Double.NaN;
                    else
                        throw ctxt.mappingException("Unexpected end-of-input when binding data into LatLng");
                case VALUE_NUMBER_FLOAT:
                    return jp.getDoubleValue();
                case VALUE_NUMBER_INT:
                    return jp.getLongValue();
                default:
                    throw ctxt.mappingException("Unexpected token (" + token.name()
                            + ") when binding data into LatLng ");
            }
        }
    }


}
