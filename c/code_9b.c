#include <stdio.h>
#include <stdlib.h>

struct Node {
    struct Node *prev;
    struct Node *next;
    int id;
    int blocks;
    int hasMoved;
};

struct Node* createNode(struct Node *prev, struct Node *next, int id, int blocks, int hasMoved) {
    struct Node *node = malloc(sizeof(struct Node));
    node->prev = prev;
    node->next = next;
    node->id = id;
    node->blocks = blocks;
    node->hasMoved = hasMoved;
    return node;
}

int main(int argc, char **argv) {
    FILE *fptr = fopen(argv[1], "r");
    struct Node *head = createNode(NULL, NULL, 0, fgetc(fptr)-0x30, 0);
    struct Node *tail = head;
    int i, j, currChar;

    for (i = 1; (currChar = fgetc(fptr)) != EOF; i++) {
        if (currChar != '0') {
            tail->next = createNode(tail, NULL, i % 2 == 0 ? i / 2 : -1, currChar - 0x30, 0);
            tail = tail->next;
        }
    }
    fclose(fptr);

    struct Node *curr, *tmp;
    while (1) {
        while (tail->id == -1 || tail->hasMoved) {
            tail = tail->prev;
        }
        if (tail == head) {
            break;
        }

        curr = head;
        while (curr != tail && (curr->id != -1 || curr->blocks < tail->blocks)) {
            curr = curr->next;
        }

        if (curr != tail) {
            curr->prev = createNode(curr->prev, curr, tail->id, tail->blocks, 1);
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
        }

        tail = tail->prev;
    }

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
