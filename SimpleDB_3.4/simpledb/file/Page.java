package simpledb.file;

import java.nio.ByteBuffer;
import java.nio.charset.*;

public class Page {
   private ByteBuffer bb;
   public static Charset CHARSET = StandardCharsets.US_ASCII;

   // For creating data buffers
   public Page(int blocksize) {
      bb = ByteBuffer.allocateDirect(blocksize);
   }
   
   // For creating log pages
   public Page(byte[] b) {
      bb = ByteBuffer.wrap(b);
   }

   public int getInt(int offset) {
      if (offset < 0 || offset+4 > bb.capacity()) {
         throw new IllegalArgumentException("Invalid offset: " + offset);
      }
      return bb.getInt(offset);
   }

   public void setInt(int offset, int n) {
      if (offset < 0 || offset+4 > bb.capacity()) {
         throw new IllegalArgumentException("Invalid offset: " + offset+ " " + bb.capacity());
      }
      bb.putInt(offset, n);
   }

   public byte[] getBytes(int offset) {
      if (offset < 0 || offset+4 > bb.capacity()) {
         throw new IllegalArgumentException("Invalid offset: " + offset);
      }
      bb.position(offset);
      int length = bb.getInt();
      if (offset+4+length > bb.capacity()) {
         throw new IllegalArgumentException("Invalid offset: " + offset);
      }
      byte[] b = new byte[length];
      bb.get(b);
      return b;
   }

   public void setBytes(int offset, byte[] b) {
      if (offset < 0 || offset+4+b.length > bb.capacity()) {
         throw new IllegalArgumentException("Invalid offset: " + offset);
      }
      bb.position(offset);
      bb.putInt(b.length);
      bb.put(b);
   }
   
   public String getString(int offset) {
      byte[] b = getBytes(offset);
      return new String(b, CHARSET);
   }

   public void setString(int offset, String s) {
      byte[] b = s.getBytes(CHARSET);
      setBytes(offset, b);
   }

   public static int maxLength(int strlen) {
      float bytesPerChar = CHARSET.newEncoder().maxBytesPerChar();
      return Integer.BYTES + (strlen * (int)bytesPerChar);
   }

   // a package private method, needed by FileMgr
   ByteBuffer contents() {
      bb.position(0);
      return bb;
   }
}
