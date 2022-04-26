//#include <stdio.h>
//        #include <stdlib.h>
//        #include "list.h"
//
//
//        void print (element_t ev) {
//        char* e = ev;
//        printf ("%s\n", e);
//        }
//
//        void printInt (element_t ev) {
//        int* e = ev;
//        printf ("%d\n", e);
//        }
//
////For map
//        void step2(element_t* t, element_t one) {
//        char* a = (char*) one;
//        int** array = (int**) t;
//
//        if (*array == NULL) {
//        *array = malloc(sizeof(int*));
//        }
//
//        char* ptr;
//        int result = strtol(a, &ptr, 10);
//        if (result == 0) result = -1;
//        *array = result;
//        }
//
//        void step3(element_t* eachOutputEle, element_t l0ele, element_t l1ele) {
//        char** arr = (char**) eachOutputEle;
//        int* val = (int*) l0ele;
//        char* str = (char*) l1ele;
//
//        if (*arr == NULL) {
//        *arr = malloc(sizeof(char*));
//        }
//
//        if (val == -1) {
//        *arr = str;
//        } else {
//        *arr = NULL;
//        }
//        }
//
//        int step4(element_t t) {
//        int cast = (int) t;
//        return (cast > 0);
//        }
//
//        int step5(element_t t) {
//        char* cast = (char*) t;
//        return (cast != NULL);
//        }
//
//        void step6(element_t* eachOutputEle, element_t l0ele, element_t l1ele) {
//        char** arr = (char** ) eachOutputEle;
//        int* val = (int*) l0ele;
//        char* str = (char*) l1ele;
//        int am = val;
//
//        if (*arr == NULL) {
//        *arr = malloc(sizeof(char*));
//        }
//
//        char result[am+1];
//        strncpy(result, str, am);
//        result[am] = '\0';
//        sprintf(*arr, "%s", result);
//        }
//
//        void helplast(element_t* rv, element_t av, element_t bv) {
//        char** top = (char**) rv;
//        if (*rv == NULL) {
//        *rv = malloc(sizeof(char*));
//        }
//        char* castA = (char*) *rv;
//        char* castB = (char*) bv;
//        char* space = (char*) " ";
//
//        strcat(castA, castB);
//        strcat(castA, space);
//        }
//
//        int main(int argc, char **argv) {
//        char* array[argc-1]; //char* array. size = 3
//
//        for (int i=0; i<argc-1; i++) {
//        array[i] = argv[i+1];
//        }
//
//        struct list* input = list_create(); //list has char*
//        list_append_array(input, (element_t*) array, argc-1);
//
//        struct list* testMap = list_create(); //list has int*
//        list_map1(step2, testMap, input);
//
//        struct list* nullStrings = list_create();
//        list_map2(step3, nullStrings, testMap,input); //num, char*
//
//        struct list* intNegRemoved = list_create();
//        list_filter(step4, intNegRemoved, testMap);
//
//        struct list* nullRemoved = list_create();
//        list_filter(step5, nullRemoved, nullStrings);
//
//        struct list* truncated = list_create();
//        list_map2(step6, truncated, intNegRemoved, nullRemoved);
//        list_foreach(print,truncated);
//
//        // - -------------------------
//
//        char* joined;
//        char** pointer = joined;
//        list_foldl(helplast, (element_t*) &pointer, truncated);
//        printf("%s", pointer);
//
//
//        // list_foreach(free, testMap);
//        // list_foreach(free, nullStrings);
//        // list_foreach(free, truncated);
//
//        list_destroy(input);
//        list_destroy(testMap);
//        list_destroy(nullStrings);
//        list_destroy(intNegRemoved);
//        list_destroy(nullRemoved);
//        list_destroy(truncated);
//        }
