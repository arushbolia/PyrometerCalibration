import javax.swing.*;
import com.fazecast.jSerialComm.SerialPort;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

public class PyroGUITemp {
       
       public static SerialPort availableComPort[] = SerialPort.getCommPorts();
       public static SerialPort firstAvailableComPort = availableComPort[0];
       public static String in_command = new String();
       public static JLabel temperature = new JLabel();
       public static JLabel statusCode = new JLabel();
       public static JLabel emitivityValue = new JLabel();
       public static JLabel fstBrightness = new JLabel();
       public static JLabel secBrightness = new JLabel();
       public static JLabel interTemperature = new JLabel();
       public static JLabel emitivitySlope = new JLabel();
       public static JLabel lblNewLabel = new JLabel("New label");
      
       static int l =0;
       
       public static void main(String[] args) {
              
              int i=1;
              int j=1;
       
              checkAndConnectToSuitableNetwork();
              
              JFrame frame = new JFrame();
              
              JLabel input = new JLabel("Enter input Command: ");
              input.setBounds(50, 70, 150, 20);
              
              JTextField in_command_tf = new JTextField();
              in_command_tf.setBounds(50, 100, 100, 20);
              
              JButton confirm = new JButton("Submit");
              confirm.setBounds(180, 100, 80, 20);
              
              //OnClick Function
              confirm.addActionListener(new ActionListener() {
                     @Override
                     public void actionPerformed(ActionEvent e) {
                           in_command = in_command_tf.getText();
                           String strData = in_command.toUpperCase();
                      String checkSum = checkSumCalculator(strData);
                      in_command = ((char)2 + strData + (char)3  + checkSum)+(char)0;   
                      System.out.println(in_command);
                      try {
                              
                             //Giving command to the Pyrometer
                             byte[] inCommandByteArray = in_command.getBytes("UTF-8");
                                  
                             firstAvailableComPort.writeBytes(inCommandByteArray, inCommandByteArray.length);
                             //System.out.println(in_command);
                             Thread.sleep(50);
                      }     catch(Exception error) { }
                           
                           mainExtend(i,j);
                           
                     }
                     
              });
              
              
              
              //Assigning places to the components
              temperature.setBounds(50, 130, 300, 20);
              statusCode.setBounds(50, 160, 300, 20);
              emitivityValue.setBounds(50, 190, 300, 20);
              emitivitySlope.setBounds(50, 220, 300, 20);
              fstBrightness.setBounds(50, 250, 300, 20);
              secBrightness.setBounds(50, 280, 350, 20);
              interTemperature.setBounds(50, 310, 300, 20);
              
              //adding components to the frame
              frame.getContentPane().add(input);
              frame.getContentPane().add(in_command_tf);
              frame.getContentPane().add(confirm);
              frame.getContentPane().add(temperature);
              frame.getContentPane().add(statusCode);
              frame.getContentPane().add(emitivityValue);
              frame.getContentPane().add(emitivitySlope);
              frame.getContentPane().add(fstBrightness);
              frame.getContentPane().add(secBrightness);
              frame.getContentPane().add(interTemperature);
              frame.getContentPane().add(emitivitySlope);
              
              frame.setSize(500, 600);
              frame.getContentPane().setLayout(null);
              
              
              lblNewLabel.setBounds(50, 440, 50, 20);
              
              
              frame.setVisible(true);
              frame.invalidate();
              frame.validate();
              frame.repaint();
              
              //Making the thread inactive for 10 seconds
              try {
				Thread.sleep(10000);
              } catch (Exception e3) { }
              
              //Recursively calling the function infinitely
              while(true) 
              {
	           	   try {
						Thread.sleep(1000);
					} catch (Exception e2) { }
	           	   
	           	   	in_command = in_command_tf.getText();
	           	   	String strData = in_command.toUpperCase();
	           	   	String checkSum1 = checkSumCalculator(strData);
	           	   	in_command = ((char)2 + strData + (char)3  + checkSum1)+(char)0;   
	           	   	System.out.println(in_command);
	           	   	
	           	   	byte[] inCommandByteArray;
	           	   	
	           	   	try {
	           	   		inCommandByteArray = in_command.getBytes("UTF-8");
	           	   		firstAvailableComPort.writeBytes(inCommandByteArray, inCommandByteArray.length);
	           	   		System.out.println(in_command);
	           	   	} catch (Exception e1) { }
	           	   	
	           	   	mainExtend(i,j);
	           	   	
	           	   	System.out.println(l);
	           	   	l++;
	           	   	lblNewLabel.setText(Integer.toString(l));
	           	   	frame.getContentPane().add(lblNewLabel);
	           	   	
              }
              
              
              
       }
       
       //Checking for suitable pytometer connection
       private static void checkAndConnectToSuitableNetwork() {
              if(firstAvailableComPort.isOpen())
              {
                     System.out.println("Check Connection again");
                     System.exit(0);
              }
              else {
                     firstAvailableComPort.setBaudRate(19200);
                  firstAvailableComPort.setParity(firstAvailableComPort.NO_PARITY);
                  firstAvailableComPort.setNumDataBits(8);
                  firstAvailableComPort.setNumStopBits(firstAvailableComPort.ONE_STOP_BIT);
                  firstAvailableComPort.openPort();
                  System.out.println("Opening the first available serial port: " + firstAvailableComPort.getDescriptivePortName());
                 
              }
              
       }
       
       //main Method
       private static void mainExtend(int i, int j){
              try {
               Scanner sc1 = new Scanner(firstAvailableComPort.getInputStream());
               String strs = sc1.nextLine();
               System.out.println(strs);
               sc1.close();
            
               checkOutCommand(strs, i, j);
               //Thread.sleep(1000);
              }
              catch(Exception e){
                     
              }
       }
       
       //
       private static void checkOutCommand(String strs, int i, int j) {
    	   String out_command = strs;
	       out_command = out_command.toUpperCase();
	              
	       char[] strarray = out_command.toCharArray();
	       String checkSumOutputManual = new String(strarray, 32+i+j, 2);
	       String checkSumOutputAuto = checkSumCalculator(out_command.substring(i, 32+i));
	       
	       //Check Validity of CheckSum
	       if(CheckSumVerification(checkSumOutputManual,checkSumOutputAuto))
	       {
	             extractRequiredData(strarray, i, j);     
	       }
	       else
	       {
	             System.out.println("Invalid Check sum.");
	       }
       }
       
      //Extracting the required data from the output command
       private static void extractRequiredData(char[] strarray, int i, int j) {
           String hex = new String(strarray,4+i,4);
           
           String status_code = new String(strarray, 8+i, 4);
           
           String emitivity = new String(strarray, 12+i, 4);
           float emitivity1 = (float)( (float) hex_to_decimal(emitivity)/1000.0);
           
           String emitivity_slope = new String(strarray, 16+i, 4);
           float emitivity_slope1 = (float)((float)hex_to_decimal(emitivity_slope) / 1000.0);
           
           String first_brightness_temp = new String(strarray, 20+i, 4);
           
           String second_brightness_temp = new String(strarray, 24+i, 4);
           
           String internal_temp = new String(strarray, 28+i, 4);
           
           //Object Initialization
           temperature.setText("Temperature is: " + hex_to_decimal(hex) + "°K or " + temperatureConverter(hex_to_decimal(hex)) + "°C");
           statusCode.setText("Status Code: " + hex_to_decimal(status_code));
           emitivityValue.setText("Emitivity: " + emitivity1);
           emitivitySlope.setText("Emitivity Slope: " + emitivity_slope1);
           fstBrightness.setText("First Brightness Temperature is: " + hex_to_decimal(first_brightness_temp) + "°K or " + temperatureConverter(hex_to_decimal(first_brightness_temp)) + "°C");
           secBrightness.setText("Second Brightness Temperature is: " + hex_to_decimal(second_brightness_temp) + "°K or " + temperatureConverter(hex_to_decimal(second_brightness_temp)) + "°C");
           interTemperature.setText("Internal Temperature is: " + hex_to_decimal(internal_temp) + "°C");
           
           //Printing data for verification purpose
           System.out.println("Temperature is: " + hex_to_decimal(hex) + "°K or " + temperatureConverter(hex_to_decimal(hex)) + "°C");
           System.out.println("Status Code: " + hex_to_decimal(status_code));
           System.out.println("Emitivity: " + emitivity1);
           System.out.println("Emitivity Slope: " + emitivity_slope1);
           System.out.println("First Brightness Temperature is: " + hex_to_decimal(first_brightness_temp) + "°K or " + temperatureConverter(hex_to_decimal(first_brightness_temp)) + "°C");
           System.out.println("Second Brightness Temperature is: " + hex_to_decimal(second_brightness_temp) + "°K or " + temperatureConverter(hex_to_decimal(second_brightness_temp)) + "°C");
           System.out.println("Internal Temperature is: " + hex_to_decimal(internal_temp) + "°C");
       }
       
       //Converting HexaDeciaml value into Decimal value
       public static int hex_to_decimal(String str)
       {
           int a =0;
           a = Integer.parseInt(str, 16);
           return a;
       }
       
       //Converting temperature in F to C
       public static int temperatureConverter(int tempF)
       {
           int tempC = 0;
           tempC = tempF - 273; 
           return tempC;
       }
       
       //Calculating checkSum
       public static String checkSumCalculator(String command)
       {
              String checkSum ="00";
              int chkSum = 0;
              char[] array = command.toCharArray();
              int hex = 0;
              
              for(char output: array)
              {
                     hex = output;
                     chkSum = chkSum + hex;
              }
              
              checkSum = ((String)(Integer.toHexString(chkSum + 3).toUpperCase())).substring(1,3);
       
              //Extracting last two values of the CheckSum
              if(checkSum.length() % 2 != 0 && checkSum.length() % 4 == 3) {
                     checkSum = "0" + checkSum;
              }
              else if(checkSum.length() % 2 != 0 && checkSum.length() % 4 == 1) {
                     checkSum = "000" + checkSum;
              }
              else if(checkSum.length() % 2 == 0 && checkSum.length() % 4 == 2)
              {
                     checkSum = "00" + checkSum;
              }
              checkSum = checkSum.substring(2, 4);
              
              return checkSum;
       }
       
       //Verifying the checkSum
       public static boolean CheckSumVerification(String string1, String srting2)
       {
              boolean validity = false;
              
              if(string1.equals(srting2))
                     validity = true;
              
              return validity;
       }
}
