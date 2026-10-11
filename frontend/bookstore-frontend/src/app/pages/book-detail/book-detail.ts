import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import {
  ActivatedRoute,
  RouterLink
} from '@angular/router';
import { Book, BookService } from '../../services/book';
import { CartService } from '../../services/cart';

@Component({
  selector: 'app-book-detail',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './book-detail.html',
  styleUrl: './book-detail.css'
})
export class BookDetail implements OnInit {

  book: Book | null = null;

  relatedBooks: Book[] = [];

  errorMessage = '';

  cartMessage = '';

  tentativeDeliveryDate = '';

  constructor(
    private route: ActivatedRoute,
    private bookService: BookService,
    private cartService: CartService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {

    const bookId = Number(
      this.route.snapshot.paramMap.get('id')
    );

    if (!bookId) {
      this.errorMessage = 'Invalid book selected.';
      return;
    }

    this.loadBook(bookId);
    this.setTentativeDeliveryDate();
  }

  loadBook(bookId: number): void {

    this.bookService
      .getBookById(bookId)
      .subscribe({

        next: (data) => {

          this.book = data;

          console.log(
            'Book details loaded:',
            this.book
          );

          this.loadRelatedBooks(
            data.category.id,
            data.id
          );

          this.cdr.detectChanges();
        },

        error: (error) => {

          console.error(
            'Error loading book:',
            error
          );

          this.errorMessage =
            'Unable to load book details.';

          this.cdr.detectChanges();
        }

      });
  }

  loadRelatedBooks(
    categoryId: number,
    currentBookId: number
  ): void {

    this.bookService
      .getBooksByCategory(categoryId)
      .subscribe({

        next: (data) => {

          this.relatedBooks = data.filter(
            (book) => book.id !== currentBookId
          );

          console.log(
            'Related books loaded:',
            this.relatedBooks
          );

          this.cdr.detectChanges();
        },

        error: (error) => {

          console.error(
            'Error loading related books:',
            error
          );
        }

      });
  }

  addToCart(): void {

    const userData = localStorage.getItem('currentUser');

    if (!userData) {
      this.errorMessage = 'Please login first.';
      return;
    }

    if (!this.book) {
      return;
    }

    const user = JSON.parse(userData);

    this.cartService
      .addToCart(user.id, this.book.id, 1)
      .subscribe({

        next: () => {

          this.cartMessage =
            'Book added to cart successfully!';

          this.errorMessage = '';

          this.cdr.detectChanges();
        },

        error: (error) => {

          console.error(
            'Error adding book to cart:',
            error
          );

          this.errorMessage =
            'Unable to add book to cart.';

          this.cartMessage = '';

          this.cdr.detectChanges();
        }

      });
  }

  setTentativeDeliveryDate(): void {

    const deliveryDate = new Date();

    deliveryDate.setDate(
      deliveryDate.getDate() + 5
    );

    this.tentativeDeliveryDate =
      deliveryDate.toISOString().split('T')[0];
  }

}
