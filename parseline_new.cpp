#include <cstdio>
#include <cstring>
#include <iostream>
using namespace std;

const int kMaxBufferSize = 1000000;
const char kSeparator = ',';

char buffer[kMaxBufferSize + 1];
int numberOfColumn = 0;

void processFirstLine() {
    fgets(buffer, kMaxBufferSize, stdin);
    int length = strlen(buffer);
    for (int i = 0; i < length; ++i) {
        if (buffer[i] == kSeparator) {
            numberOfColumn++;
        }
    }
    numberOfColumn++;
    //    cout << numberOfColumn << endl;
}

void processContent() {
    while (true) {
        int ch = getchar();
        if (ch == EOF) {
            break;
        }
        for (int c = 0; c < numberOfColumn; ++c) {
            if (ch == '"') {
                int length = 0;
                int last = 0;
                //memset(buffer, 0, 10000);
                buffer[0] = buffer[1] = buffer[2] = buffer[3] = '\0';
                ch = getchar();
                do {
                    if (ch == '"') {
                        ch = getchar();
                        if (ch == '"') {
                            continue;
                        } else { // ch == ','
                            break;
                        }
                    }
                    if (ch == kSeparator) {
                        last = ch;
                        buffer[length++] = ' ';
                        continue;
                    }
                    if (ch == '\n') {
                        last = ch;
                        buffer[length++] = ' ';
                        continue;
                    }
                    last = ch;
                    buffer[length++] = ch;
                } while ((ch = getchar()) && ch != EOF);
                buffer[length++] = '\0';
                printf("%s", buffer);
            } else {
                int length = 0;
                do {
                    if (ch == kSeparator || ch == '\n') {
                        break;
                    }
                    buffer[length++] = ch;
                } while ((ch = getchar()) && ch != EOF);
                buffer[length++] = '\0';
                printf("%s", buffer);
            }
            if (c < numberOfColumn - 1) {
                printf(",");
                ch = getchar();
            } else {
              if (buffer != string("not a real question") &&
                  buffer != string("not constructive") &&
                  buffer != string("off topic") &&
                  buffer != string("open") &&
                  buffer != string("too localized")) {
                return;
              }
            }
        }
        printf("\n");
    }
}

int main() {
    processFirstLine();
    processContent();
    return 0;
}
