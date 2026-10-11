
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { Book, BookService } from '../../services/book';
import { CartService } from '../../services/cart';
import {
  RecommendationBook,
  RecommendationService
} from '../../services/recommendation';
import {
  Category,
  CategoryService
} from '../../services/category';
import {
  Brand,
  BrandService
} from '../../services/brand';

@Component({
  selector: 'app-books',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './books.html',
  styleUrl: './books.css'
})
export class Books implements OnInit {

  books: Book[] = [];
  recommendations: RecommendationBook[] = [];

  categories: Category[] = [];
  brands: Brand[] = [];

  selectedCategoryId: number | null = null;
  selectedBrandId: number | null = null;

  errorMessage = '';
  cartMessage = '';

  cartQuantities: { [bookId: number]: number } = {};

  constructor(
    private bookService: BookService,
    private cartService: CartService,
    private recommendationService: RecommendationService,
    private categoryService: CategoryService,
    private brandService: BrandService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadBooks();
    this.loadRecommendations();
    this.loadCategories();
    this.loadBrands();
  }

  logout(): void {
    localStorage.removeItem('currentUser');
    this.router.navigate(['/login']);
  }

  loadBooks(): void {
    this.bookService.getBooks().subscribe({
      next: (data) => {
        this.books = data;

        console.log('Books loaded:', this.books);
        console.log('Number of books:', this.books.length);

        this.cdr.detectChanges();
      },

      error: (error) => {
        console.error('Error loading books:', error);
        this.errorMessage = 'Unable to load books.';
        this.cdr.detectChanges();
      }
    });
  }

  loadRecommendations(): void {
    const userData = localStorage.getItem('currentUser');

    if (!userData) {
      return;
    }

    const user = JSON.parse(userData);

    this.recommendationService
      .getRecommendations(user.id)
      .subscribe({
        next: (data) => {
          this.recommendations = data;

          console.log('Recommendations loaded:', this.recommendations);
          this.cdr.detectChanges();
        },

        error: (error) => {
          console.error('Error loading recommendations:', error);
        }
      });
  }

  loadCategories(): void {
    this.categoryService.getCategories().subscribe({
      next: (data) => {
        this.categories = data;

        console.log('Categories loaded:', this.categories);
        this.cdr.detectChanges();
      },

      error: (error) => {
        console.error('Error loading categories:', error);
        this.errorMessage = 'Unable to load categories.';
        this.cdr.detectChanges();
      }
    });
  }

  loadBrands(): void {
    this.brandService.getBrands().subscribe({
      next: (data) => {
        this.brands = data;

        console.log('Brands loaded:', this.brands);
        this.cdr.detectChanges();
      },

      error: (error) => {
        console.error('Error loading brands:', error);
        this.errorMessage = 'Unable to load brands.';
        this.cdr.detectChanges();
      }
    });
  }

  selectCategory(categoryId: number): void {
    this.selectedCategoryId = categoryId;
    this.selectedBrandId = null;

    this.categoryService
      .getBooksByCategory(categoryId)
      .subscribe({
        next: (data) => {
          this.books = data;
          console.log('Books for category:', this.books);
          this.cdr.detectChanges();
        },

        error: (error) => {
          console.error('Error loading category books:', error);
          this.errorMessage = 'Unable to load books for this category.';
          this.cdr.detectChanges();
        }
      });
  }

  showAllBooks(): void {
    this.selectedCategoryId = null;
    this.selectedBrandId = null;
    this.loadBooks();
  }

  selectBrand(brandId: number): void {
    this.selectedBrandId = brandId;
    this.selectedCategoryId = null;

    this.brandService
      .getBooksByBrand(brandId)
      .subscribe({
        next: (data) => {
          this.books = data;
          console.log('Books for brand:', this.books);
          this.cdr.detectChanges();
        },

        error: (error) => {
          console.error('Error loading brand books:', error);
          this.errorMessage = 'Unable to load books for this brand.';
          this.cdr.detectChanges();
        }
      });
  }

  addToCart(bookId: number): void {
    const userData = localStorage.getItem('currentUser');

    if (!userData) {
      this.errorMessage = 'Please login first.';
      return;
    }

    const user = JSON.parse(userData);

    this.cartService.addToCart(user.id, bookId, 1).subscribe({
      next: () => {
        this.cartQuantities[bookId] =
          (this.cartQuantities[bookId] || 0) + 1;

        this.cartMessage = 'Book added to cart!';
        this.errorMessage = '';

        console.log('Book added to cart:', bookId);
        console.log('Quantity:', this.cartQuantities[bookId]);

        this.cdr.detectChanges();
      },

      error: (error) => {
        console.error('Error adding book to cart:', error);
        this.errorMessage = 'Unable to add book to cart.';
        this.cartMessage = '';
        this.cdr.detectChanges();
      }
    });
  }
}
