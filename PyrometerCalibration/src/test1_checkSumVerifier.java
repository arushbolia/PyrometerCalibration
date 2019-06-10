
public class test1_checkSumVerifier {

	public static void main(String[] args) {
		String convert = "01RD026E0000039803E811F311F3001B0115";
		System.out.println(convert);
		char[] array = convert.toCharArray();
		int l= 0;
		for(char output: array)
		{
			l = l + (int)output;
			System.out.println("Output: " + output + " Sum: " + l + " value: " + (int)output);
		}
		l = l  +3;
		System.out.println( " Sum: " + l + " Its hex value: " + Integer.toHexString(l).substring(1, 3).toUpperCase());
	}

}


//CheckSum Verifier in VB
/*
Imports System

Public Class Test
    Public Shared Sub Main()
        Dim strData As String = "01RD0E000130"            						'''' This is command u have to send to Pyrometer to read temp
        Dim szArr() As Char
        szArr = strData.ToCharArray
        Dim chksum As Integer = 0, hexvalue As Integer = 0
        Dim i As Integer = 0
        System.Console.WriteLine("Input String: " & strData)
        For i = 0 To strData.Length - 1                                        	'''' This is Checksum for device for data to send 
            hexvalue = Convert.ToInt32(szArr(i))
            chksum = chksum + hexvalue
            System.Console.WriteLine(" Element: " & szArr(i) & " HexValue: " & hexvalue & " ChkSum: " & chksum)
        Next i
        System.Console.WriteLine("ChkSum final: " & chksum+3 )
        Dim strchksum As String= Mid(Hex(CLng(chksum + 3)), 2, 2)
        Dim strcmd As String = New String(Chr(2) + strData + Chr(3) + strchksum)
        System.Console.WriteLine ("StrChkSum: " & strchksum & " StrCmd:  " & strcmd )
    End Sub
End Class


*/
