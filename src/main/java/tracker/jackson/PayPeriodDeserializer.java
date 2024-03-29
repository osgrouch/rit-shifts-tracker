package tracker.jackson;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import tracker.PayPeriod;
import tracker.Shift;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class to deserialize {@link PayPeriod} and {@link Shift} objects with a
 * {@link com.fasterxml.jackson.databind.ObjectMapper Jackson ObjectMapper}.
 */
public class PayPeriodDeserializer extends StdDeserializer<PayPeriod> {
	public PayPeriodDeserializer() {
		this(null);
	}

	public PayPeriodDeserializer(Class<PayPeriod> payPeriod) {
		super(payPeriod);
	}

	@Override
	public PayPeriod deserialize(JsonParser parser, DeserializationContext context) throws IOException, JacksonException {
		ObjectCodec codec = parser.getCodec();
		JsonNode payPeriodNode = codec.readTree(parser);

		String start = payPeriodNode.get("start").textValue();
		List<Shift> shifts = new ArrayList<>();
		JsonNode shiftsNode = payPeriodNode.get("shifts");
		Iterator<JsonNode> it = shiftsNode.elements();
		while (it.hasNext()) {
			JsonNode shiftNode = it.next();
			String location = shiftNode.get("location").textValue();
			String date = shiftNode.get("date").textValue();
			String in = shiftNode.get("in").textValue();
			String out = shiftNode.get("out").textValue();
			double payRate = shiftNode.get("payRate").doubleValue();
			shifts.add(new Shift(location, date, in, out, payRate));
		}

		return new PayPeriod(start, null, shifts);
	}
}
