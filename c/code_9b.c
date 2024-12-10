#include <stdio.h>
#include <stdlib.h>

struct Node {
    struct Node *prev;
    struct Node *next;
    int id;
    int blocks;
    int hasMoved;
};

void printList(struct Node *head) {
    struct Node *curr = head;
    while (curr) {
        if (curr->id >= 0) {
            printf("[%d: %d] ", curr->id, curr->blocks);
        } else {
            printf("[Free: %d] ", curr->blocks);
        }
        curr = curr->next;
    }
    printf("\n");
}

int main(int argc, char **argv) {
    FILE *fptr = NULL;
    struct Node *head = malloc(sizeof(struct Node));
    struct Node *tail = head;
    int i, currChar;

    i = 0;
    fptr = fopen(argv[1], "r");
    while ((currChar = fgetc(fptr)) != EOF) {
        if (currChar == '0') {
            i++;
            continue;
        }

        if (i % 2) {
            tail->id = -1;
        } else {
            tail->id = i / 2;
        }
        tail->blocks = currChar - 0x30; // Parse char as int
        tail->hasMoved = 0;
        tail->next = malloc(sizeof(struct Node));
        tail->next->prev = tail;
        tail = tail->next;
        i++;
    }
    fclose(fptr);

    // Accidentally allocated extra node
    tail = tail->prev;
    free(tail->next);
    tail->next = NULL;

    // printList(head);

    struct Node *curr, *tmp;
    while (tail != head) {
        while (tail->id == -1 || tail->hasMoved) {
            tail = tail->prev;
        }
        if (tail == head) {
            break;
        }

        curr = head;
        while (curr && curr != tail && (curr->id != -1 || curr->blocks < tail->blocks)) {
            curr = curr->next;
        }

        if (curr && curr != tail) {
            // Create new node
            tmp = curr->prev;
            curr->prev = malloc(sizeof(struct Node));
            curr->prev->prev = tmp;
            curr->prev->next = curr;
            curr->prev->id = tail->id;
            curr->prev->blocks = tail->blocks;
            curr->prev->hasMoved = 1;

            // Adjust list around it
            curr->prev->prev->next = curr->prev;
            tail->id = -1;
            curr->blocks -= tail->blocks;

            // Dealloc zero blocks
            if (curr->blocks == 0) {
                curr->prev->next = curr->next;
                curr->next->prev = curr->prev;
                free(curr);
            }

            // Merge free blocks
            if (tail->next && tail->next->id == -1) {
                tmp = tail->next;
                tail->blocks += tail->next->blocks;
                tail->next = tail->next->next;
                if (tail->next) {
                    tail->next->prev = tail;
                }
                free(tmp);
            }
            if (tail->prev && tail->prev->id == -1) {
                tmp = tail->prev;
                tail->blocks += tail->prev->blocks;
                tail->prev = tail->prev->prev;
                if (tail->prev) {
                    tail->prev->next = tail;
                }
                free(tmp);
            }

            // printList(head);
        }

        tail = tail->prev;
    }

    // printList(head);

    int j;
    unsigned long long sum = 0;
    i = 0;
    curr = head;
    while (curr) {
        for (j = 0; j < curr->blocks; i++, j++) {
            sum += (curr->id == -1 ? 0 : curr->id) * i;
        }
        curr = curr->next;
    }
    printf("%lld\n", sum);
    return 0;
}