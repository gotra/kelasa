package in.kelasa.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.maps.model.LatLng;

import java.io.IOException;

/**
 * Created by rajeevguru on 30/11/15.
 */
public class CustomLatLngSerializer extends JsonSerializer<LatLng> {



        public static final long[] POW10 = new long[]{1L, 10L, 100L, 1000L, 10000L, 100000L, 1000000L, 10000000L, 100000000L, 1000000000L};

        public CustomLatLngSerializer() {
        }

    @Override
    public void serialize(LatLng value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartArray();
        jsonGenerator.writeNumber(value.lng);
        jsonGenerator.writeNumber(value.lat);
        jsonGenerator.writeEndArray();
    }






    protected static String fastDoubleToString(double val, int precision) {
            StringBuilder sb = new StringBuilder();
            if(val < 0.0D) {
                sb.append('-');
                val = -val;
            }

            long exp = POW10[precision];
            long lval = (long)(val * (double)exp + 0.5D);
            sb.append(lval / exp).append('.');
            long fval = lval % exp;

            int i;
            for(i = precision - 1; i > 0 && fval < POW10[i] && fval > 0L; --i) {
                sb.append('0');
            }

            sb.append(fval);

            for(i = sb.length() - 1; sb.charAt(i) == 48 && sb.charAt(i - 1) != 46; --i) {
                sb.deleteCharAt(i);
            }

            return sb.toString();
        }





}
