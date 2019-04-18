  0         LOADL        0
  1         CALL         newarr  
  2         CALL         L10
  3         HALT   (0)   
  4  L10:   LOADL        -1
  5         LOADL        2
  6         CALL         newobj  
  7         LOAD         3[LB]
  8         LOADL        0
  9         LOADL        0
 10         CALL         fieldupd
 11         LOADL        2
 12         JUMP         L12
 13  L11:   LOADL        -1
 14         LOADL        2
 15         CALL         newobj  
 16         LOAD         5[LB]
 17         LOADL        0
 18         LOAD         4[LB]
 19         CALL         fieldupd
 20         LOAD         5[LB]
 21         LOAD         3[LB]
 22         CALLI        L13
 23         LOAD         4[LB]
 24         LOADL        2
 25         CALL         mult    
 26         STORE        4[LB]
 27         POP          1
 28  L12:   LOAD         4[LB]
 29         LOADL        50
 30         CALL         lt      
 31         JUMPIF (1)   L11
 32         LOAD         3[LB]
 33         CALLI        L17
 34         LOAD         3[LB]
 35         LOADL        1
 36         CALL         fieldref
 37         LOADL        1
 38         CALL         fieldref
 39         LOADL        1
 40         CALL         fieldref
 41         LOADL        1
 42         CALL         fieldref
 43         LOADL        1
 44         CALL         fieldref
 45         LOADL        1
 46         CALL         fieldref
 47         LOADL        0
 48         CALL         fieldref
 49         LOADL        1000
 50         CALL         add     
 51         CALL         putintnl
 52         RETURN (0)   1
 53  L13:   LOAD         1[OB]
 54         LOADL        0
 55         CALL         eq      
 56         JUMPIF (1)   L14
 57         JUMP         L15
 58  L14:   LOAD         -1[LB]
 59         STORE        1[OB]
 60         LOADL        -1
 61         LOADL        2
 62         CALL         newobj  
 63         LOAD         3[LB]
 64         LOADL        0
 65         LOAD         -1[LB]
 66         LOADL        0
 67         CALL         fieldref
 68         LOADL        4
 69         CALL         add     
 70         CALL         fieldupd
 71         LOAD         1[OB]
 72         LOADL        1
 73         LOAD         3[LB]
 74         CALL         fieldupd
 75         POP          1
 76         JUMP         L16
 77  L15:   LOAD         -1[LB]
 78         LOAD         1[OB]
 79         CALLI        L13
 80         POP          0
 81         JUMP         L16
 82  L16:   RETURN (0)   1
 83  L17:   LOADA        0[OB]
 84         LOADL        0
 85         CALL         fieldref
 86         CALL         putintnl
 87         LOAD         1[OB]
 88         LOADL        0
 89         CALL         ne      
 90         JUMPIF (1)   L18
 91         JUMP         L19
 92  L18:   LOAD         1[OB]
 93         CALLI        L17
 94         POP          0
 95  L19:   RETURN (0)   0
