package tracker.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import tracker.PayPeriod;
import tracker.Shift;

import java.io.IOException;

/**
 * Class to serialize {@link PayPeriod} and {@link Shift} objects with a
 * {@link com.fasterxml.jackson.databind.ObjectMapper Jackson ObjectMapper}.
 */
public class PayPeriodSerializer extends StdSerializer<PayPeriod> {
	public PayPeriodSerializer() {
		this(null);
	}

	public PayPeriodSerializer(Class<PayPeriod> payPeriod) {
		super(payPeriod);
	}

	/**
	 * Round the given number to two decimal places.
	 *
	 * @param db Number to round.
	 * @return Number with 2 decimal places.
	 */
	private double roundDouble(double db) {
		return Math.round(db * 100) / 100.0;
	}

	@Override
	public void serialize(PayPeriod payPeriod, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeStartObject();
		gen.writeStringField("start", payPeriod.getStart());
		gen.writeStringField("end", payPeriod.getEnd());
		gen.writeNumberField("hours", roundDouble(payPeriod.getHours()));
		gen.writeNumberField("pay", roundDouble(payPeriod.getPay()));
		gen.writeArrayFieldStart("shifts");
		for (Shift shift : payPeriod.getShifts()) {
			gen.writeObject(shift);
		}
		gen.writeEndArray();
		gen.writeEndObject();
	}
}
