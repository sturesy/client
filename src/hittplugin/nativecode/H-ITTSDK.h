/* 
=============================================================================

      h-ittsdk.h -- H-ITT SDK C/C++ Header File  

      strict ANSI c for compatability
	
      Copyright (c) 2001-2009 www.h-itt.com

      For more information and technical support visit www.h-itt.com

============================================================================
*/

#define __STDCALL__ __stdcall

#ifdef __MAC__
#define __STDCALL__
#endif

#ifdef __LINUX__
#define __STDCALL__
#endif


#ifdef __cplusplus
extern "C" {  
/* only need to export C interface if used by C++ source code*/
#endif
/*!

returns the version number of the SDK. 

version ... buffer to hold version number. must be a minumum of 10 charaters
len     ... the length of the buffer.

*/
int __STDCALL__ hitt_sdk_version(char *version,unsigned int len);

/* return codes */
#define HITT_ERROR 1
#define HITT_OK 0

/*!

hitt_inspect - use to get remote ID number and key pressed from bytes 
read on COM port

must send minimum 10 bytes in pBytes to this function

Return values:

HITT_OK:  
	function call was successful
	pid will containt the Remote ID number and 
	pkey_code will contain the numeric key code
	
HITT_ERROR:
	function call was not successful. Throw away first byte and tack
    next set of bytes read on end and call again.
*/

/* Key codes */
#define HITT_INSPECT_NUM_BYTES 10
#define HITT_KEY_A 1
#define HITT_KEY_B 2
#define HITT_KEY_C 3
#define HITT_KEY_D 4
#define HITT_KEY_E 5
#define HITT_KEY_F 6
#define HITT_KEY_G 7
#define HITT_KEY_H 8
#define HITT_KEY_I 9
#define HITT_KEY_J 10
#define HITT_KEY_FORWARD 0
#define HITT_KEY_REVERSE 11

int __STDCALL__ hitt_inspect(unsigned char *pBytes,unsigned int *pid, unsigned int *pkey_code);

/*! 

hitt_action_packet - use to form action packet of 5 bytes 
to send out on COM port

 pBytes must be an array of length >= 5

 returns:

  HITT_OK   ....   success

  HITT_ERROR   ....   error - either invalid code
		or array not proper length

*/


/* Action Codes */
#define HITT_AC_RED						0
#define HITT_AC_GREEN					1
#define HITT_AC_YELLOW					2
#define HITT_AC_BLINK_RED				3
#define HITT_AC_BLINK_GREEN				4
#define HITT_AC_BLINK_YELLOW			5
#define HITT_AC_TOGGLE_RED_GREEN		6
#define HITT_AC_FAST_BLINK_RED			7
#define HITT_AC_FAST_BLINK_GREEN		8
#define HITT_AC_FAST_BLINK_YELLOW		9
#define HITT_AC_FAST_TOGGLE_RED_GREEN	10
#define HITT_AC_OFF						11

int __STDCALL__ hitt_action_packet(unsigned char *pBytes,unsigned int id, unsigned int action_code);

/*!
 hitt_drid_inspect - use to get remote ID and other data from LCD remotes.
 must first put base unit in DRID mode see hitt_base_unit_packet.  Then collect
 32 bytes (HITT_DRID_INSPECT_NUM_BYTES) and the pass to function
 If return is HITT_OK data is valid. 
 If return is HITT_ERROR discard first byte then tack on additional bytes read from port and reprocess

 returns:

  HITT_OK   ....   success

  HITT_ERROR   ....   error - either invalid code or null pointers

	if drid_type == HITT_DRID_TESTING_MODE then 
	question_number contains the valid question number and 
	assignment_number contains the valid assignment number
	otherwise question_number and assignment_number are ignored

	remotes send up to max 20 characters so declare your buffers to 
	hold 21 (HITT_DATA_BUFFER_LEN) to account for the NULL character
	at the end.

*/

/* drid_types */
#define HITT_DRID_INSPECT_NUM_BYTES 32
#define HITT_DRID_NORMAL		1
#define HITT_DRID_ROSTER_MODE	2
#define HITT_DRID_TESTING_MODE	64
#define HITT_DATA_BUFFER_LEN    21

int __STDCALL__ hitt_drid_inspect(unsigned char *pBytes, unsigned int *id, unsigned int *drid_type, unsigned int *question_number, unsigned int *assignment_number, char *data);

/*!

hitt_base_unit_mode  - use to get packet to send to base units to put it into DRID (long packet) or normal multiple choice (short packet mode)

 returns:

  HITT_OK   ....   success

  HITT_ERROR   ....   error - invalid code or null pointer

*/

#define HITT_BASE_UNIT_NORMAL		0 
#define HITT_BASE_UNIT_DRID 		1

int __STDCALL__ hitt_base_unit_mode(unsigned char *pBytes, unsigned int mode);

#ifdef __cplusplus
}
#endif

