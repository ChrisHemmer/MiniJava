  0         LOADL        0
  1         CALL         newarr  
  2         CALL         L14
  3         HALT   (0)   
  4         LOADL        0
  5         JUMP         L11
  6  L10:   LOAD         -1[LB]
  7         LOAD         3[LB]
  8         CALL         arrayref
  9         CALL         putintnl
 10         LOAD         3[LB]
 11         LOADL        1
 12         CALL         add     
 13         STORE        3[LB]
 14         POP          0
 15  L11:   LOAD         3[LB]
 16         LOAD         -1[LB]
 17         CALL         arraylen
 18         CALL         lt      
 19         JUMPIF (1)   L10
 20         RETURN (0)   1
 21         LOADL        0
 22         JUMP         L13
 23  L12:   LOAD         -1[LB]
 24         LOAD         3[LB]
 25         CALL         arrayref
 26         LOAD         -1[LB]
 27         LOAD         3[LB]
 28         LOAD         -1[LB]
 29         LOAD         -1[LB]
 30         CALL         arraylen
 31         LOADL        1
 32         CALL         sub     
 33         LOAD         3[LB]
 34         CALL         sub     
 35         CALL         arrayref
 36         CALL         arrayupd
 37         LOAD         -1[LB]
 38         LOAD         -1[LB]
 39         CALL         arraylen
 40         LOADL        1
 41         CALL         sub     
 42         LOAD         3[LB]
 43         CALL         sub     
 44         LOAD         4[LB]
 45         CALL         arrayupd
 46         LOAD         3[LB]
 47         LOADL        1
 48         CALL         add     
 49         STORE        3[LB]
 50         POP          1
 51  L13:   LOAD         3[LB]
 52         LOAD         -1[LB]
 53         CALL         arraylen
 54         LOADL        2
 55         CALL         div     
 56         CALL         lt      
 57         JUMPIF (1)   L12
 58         RETURN (0)   1
 59  L14:   LOADL        10
 60         CALL         newarr  
 61         LOADL        0
 62         JUMP         L16
 63  L15:   LOAD         3[LB]
 64         LOAD         4[LB]
 65         LOAD         4[LB]
 66         CALL         arrayupd
 67         LOAD         4[LB]
 68         LOADL        1
 69         CALL         add     
 70         STORE        4[LB]
 71         POP          0
 72  L16:   LOAD         4[LB]
 73         LOAD         3[LB]
 74         CALL         arraylen
 75         CALL         lt      
 76         JUMPIF (1)   L15
 77         LOAD         3[LB]
 78         CALL         ***
 79         LOAD         3[LB]
 80         CALL         ***
 81         LOADL        100
 82         CALL         putintnl
 83         LOAD         3[LB]
 84         CALL         L17
 85         LOADL        100
 86         CALL         putintnl
 87         LOAD         3[LB]
 88         CALL         ***
 89         RETURN (0)   1
 90  L17:   LOADL        2
 91         JUMP         L19
 92  L18:   LOAD         3[LB]
 93         LOADL        1
 94         CALL         add     
 95         STORE        3[LB]
 96         POP          0
 97  L19:   LOAD         -1[LB]
 98         LOAD         3[LB]
 99         CALL         arrayref
100         LOADL        0
101         CALL         ne      
102         JUMPIF (1)   L18
103         LOAD         -1[LB]
104         LOAD         3[LB]
105         CALL         arrayref
106         CALL         putintnl
107         LOAD         -1[LB]
108         LOAD         3[LB]
109         CALL         arrayref
110         CALL         putintnl
111         LOAD         -1[LB]
112         LOAD         3[LB]
113         CALL         arrayref
114         CALL         putintnl
115         RETURN (0)   1
