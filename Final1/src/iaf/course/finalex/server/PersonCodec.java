package iaf.course.finalex.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Objects;

import iaf.course.finalex.model.Person;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

public class PersonCodec implements MessageCodec<Person, Person> {

	@Override
	public void encodeToWire(Buffer buffer, Person s) {
		Objects.requireNonNull(s);
		Objects.requireNonNull(buffer);
		
		try {
			byte[] data = serialize(s);
			int len = data.length;
			buffer.appendInt(len).appendBytes(data);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Person decodeFromWire(int pos, Buffer buffer) {
		try {
			int len = buffer.getInt(pos);
			byte[] data = buffer.getBytes(pos+4, pos+4+len);
			return (Person) deserialize(data);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Person transform(Person s) {
		return s;
	}

	@Override
	public String name() {
		return "PersonCodec";
	}

	@Override
	public byte systemCodecID() {
		return -1;
	}
	
	private static byte[] serialize(Object obj) throws IOException {
        try(ByteArrayOutputStream b = new ByteArrayOutputStream()){
            try(ObjectOutputStream o = new ObjectOutputStream(b)){
                o.writeObject(obj);
            }
            return b.toByteArray();
        }
    }

	private static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        try(ByteArrayInputStream b = new ByteArrayInputStream(bytes)){
            try(ObjectInputStream o = new ObjectInputStream(b)){
                return o.readObject();
            }
        }
    }

}
