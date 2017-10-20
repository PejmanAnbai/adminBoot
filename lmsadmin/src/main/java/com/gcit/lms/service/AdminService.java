package com.gcit.lms.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gcit.lms.dao.AuthorDAO;
import com.gcit.lms.dao.BookCopiesDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.dao.BookLoansDAO;
import com.gcit.lms.dao.BorrowerDAO;
import com.gcit.lms.dao.GenreDAO;
import com.gcit.lms.dao.LibraryBranchDAO;
import com.gcit.lms.dao.PublisherDAO;
import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookCopies;
import com.gcit.lms.entity.BookLoans;
import com.gcit.lms.entity.Borrower;
import com.gcit.lms.entity.Genre;
import com.gcit.lms.entity.LibraryBranch;
import com.gcit.lms.entity.Publisher;

@RestController
public class AdminService {

	@Autowired
	AuthorDAO adao;
	@Autowired
	BookDAO bdao;
	@Autowired
	BorrowerDAO brdao;
	@Autowired
	PublisherDAO pdao;
	@Autowired
	LibraryBranchDAO ldao;
	@Autowired
	GenreDAO gdao;
	@Autowired
	BookCopiesDAO bcdao;
	@Autowired
	BookLoansDAO bldao;

	@RequestMapping(value = "/initAuthor", method = RequestMethod.GET, produces="application/json")
	public Author initAuthor() throws SQLException {
		return new Author();
	}
	
	@Transactional
	@RequestMapping(value = "/Admin/Author", method = RequestMethod.POST, consumes = "application/json", produces = { "application/json",
	"application/xml" })
	public void saveAuthor(@RequestBody Author author) throws SQLException {
		if (author.getAuthorId() != null) {
			adao.updateAuthor(author);
			adao.deleteBookAuthor(author);
		} else {
			int id = adao.saveAuthorWithID(author);
			author.setAuthorId(id);
		}
		if (author.getBooks() != null && author.getBooks().size() > 0)
			adao.saveBookAuthor(author);
	}

	@Transactional
	@RequestMapping(value = "/Admin/authorsCount", method = RequestMethod.GET, produces = { "application/json",
			"application/xml" })
	public Integer getAuthorsCount() throws SQLException {
		return adao.getAuthorsCount();
	}

	@Transactional
	@RequestMapping(value = "/Admin/Authors/Name", method = RequestMethod.GET, produces = { "application/json",
			"application/xml" })
	public List<Author> readAuthors(@RequestParam(value = "searchString", required = false) String searchString, @RequestParam(value = "pageNo", required = false) Integer pageNo)
			throws SQLException {
		List<Author> authors = adao.readAuthors(searchString, pageNo);
		for (Author a : authors) {
			a.setBooks(bdao.readAllBooksByAuthor(a));
		}
		return authors;
	}

	@Transactional
	@RequestMapping(value = "/Admin/Authors", method = RequestMethod.GET, produces = { "application/json",
			"application/xml" })
	public List<Author> readAuthors() throws SQLException {
		List<Author> authors = adao.readAuthors();
		for (Author a : authors) {
			a.setBooks(bdao.readAllBooksByAuthor(a));
		}
		return authors;
	}

	@RequestMapping(value = "/Admin/Author/authorId/{id}", method = RequestMethod.GET, produces = { "application/json",
	"application/xml" })
	public Author readAuthorByPK(@PathVariable Integer id) throws SQLException {
		Author author = adao.readAuthorByPK(id);
		author.setBooks(bdao.readAllBooksByAuthor(author));
		return author;
	}

	@Transactional
	@RequestMapping(value = "/Admin/Author", method = RequestMethod.DELETE, produces = { "application/json",
	"application/xml" })
	public void deleteAuthor(@RequestParam Integer id) throws SQLException {
		adao.deleteAuthor(id);
	}

	@Transactional
	@RequestMapping(value = "/Admin/Genre", method = RequestMethod.POST, consumes = "application/json")
	public void saveGenre(@RequestBody Genre genre) throws SQLException {
		if (genre.getGenreId() != null) {
			gdao.updateGenre(genre);
			gdao.deleteBookGenre(genre);
		} else {
			int id = gdao.saveGenreID(genre);
			genre.setGenreId(id);
		}
		if (genre.getBooks() != null && genre.getBooks().size() > 0)
			gdao.saveBookGenre(genre);
	}

	@Transactional
	@RequestMapping(value = "/Admin/genresCount", method = RequestMethod.GET, produces = { "application/json",
	"application/xml" })
	public Integer getGenresCount() throws SQLException {
		return gdao.getGenresCount();
	}

	@Transactional
	@RequestMapping(value = "/Admin/Genre/genreId/{id}", method = RequestMethod.GET, produces = { "application/json",
	"application/xml" })
	public Genre readGenreByPK(@PathVariable Integer id) throws SQLException {
		Genre genre = gdao.readGenreByPK(id);
		genre.setBooks(bdao.readAllBooksByGenre(genre));
		return genre;
	}

	@Transactional
	@RequestMapping(value = "/Admin/Genres", method = RequestMethod.GET, produces = { "application/json",
	"application/xml" })
	public List<Genre> readGenres() throws SQLException {
		List<Genre> genres = gdao.readGenres();

		for (Genre g : genres) {
			g.setBooks(bdao.readAllBooksByGenre(g));
		}
		return genres;
	}

	@Transactional
	@RequestMapping(value = "/Admin/Genres/Name", method = RequestMethod.GET, produces = { "application/json",
	"application/xml" })
	public List<Genre> readGenres(@RequestParam String searchString, @RequestParam Integer pageNo) throws SQLException {
		List<Genre> genres = gdao.readGenres(searchString, pageNo);

		for (Genre g : genres) {
			g.setBooks(bdao.readAllBooksByGenre(g));
		}
		return genres;
	}

	@Transactional
	@RequestMapping(value = "/Admin/Genre", method = RequestMethod.DELETE, produces = { "application/json",
	"application/xml" })
	public void deleteGenre(@RequestParam Integer id) throws SQLException {
		gdao.deleteGenre(id);
	}

	@Transactional
	@RequestMapping(value = "/Admin/Publisher", method = RequestMethod.POST, consumes = "application/json")
	public void savePublisher(@RequestBody Publisher publisher) throws SQLException {
		if (publisher.getPublisherId() != null) {
			pdao.updatePublisher(publisher);
		} else {
			pdao.savePublisher(publisher);
		}
	}

	@Transactional
	@RequestMapping(value = "/Admin/Publisher/publisherId/{id}", method = RequestMethod.GET, produces = { "application/json",
	"application/xml" })
	public Publisher readPublisherByPK(@PathVariable Integer id) throws SQLException {

		Publisher publisher = pdao.readPublisherByPK(id);
		publisher.setBooks(bdao.readAllBooksByPublisher(publisher));
		return publisher;
	}

	@Transactional
	@RequestMapping(value = "/Admin/publishersCount", method = RequestMethod.GET, produces = { "application/json",
	"application/xml" })
	public Integer getPublisherCount() throws SQLException {
		return pdao.getPublisherCount();
	}

	@Transactional
	@RequestMapping(value = "/Admin/Publishers/Name", method = RequestMethod.GET, produces = { "application/json",
	"application/xml" })
	public List<Publisher> readPublishers(@RequestParam String searchString, @RequestParam Integer pageNo)
			throws SQLException {
		List<Publisher> publisher = pdao.readPublishers(searchString, pageNo);
		for (Publisher p : publisher)
			p.setBooks(bdao.readAllBooksByPublisher(p));
		return publisher;
	}

	@Transactional
	@RequestMapping(value = "/Admin/Publishers", method = RequestMethod.GET, produces = { "application/json",
	"application/xml" })
	public List<Publisher> readPublishers() throws SQLException {
		List<Publisher> publisher = pdao.readPublisher(null);
		for (Publisher p : publisher)
			p.setBooks(bdao.readAllBooksByPublisher(p));
		return publisher;
	}

	@Transactional
	@RequestMapping(value = "/Admin/Publisher", method = RequestMethod.DELETE, produces = { "application/json",
	"application/xml" })
	public void deletePublisher(@RequestParam Integer id) throws SQLException {
		pdao.deletePublisher(id);
	}

	@Transactional
	@RequestMapping(value = "/Admin/Book", method = RequestMethod.POST, consumes = "application/json")
	public void saveBook(@RequestBody Book book) throws SQLException {
		if (book.getBookId() != null) {
			bdao.updateBook(book);
			bdao.deleteBookGenre(book);
			bdao.deleteBookAuthor(book);
		} else {
			int id = bdao.saveBookID(book);
			book.setBookId(id);
		}
		if (book.getGenres() != null && book.getGenres().size() > 0)
			bdao.saveBookGenre(book);
		if (book.getAuthors() != null && book.getAuthors().size() > 0)
			bdao.saveBookAuthor(book);
	}

	@Transactional
	@RequestMapping(value = "/Admin/booksCounts", method = RequestMethod.GET, produces = { "application/json",
	"application/xml" })
	public Integer getBooksCount() throws SQLException {
		return bdao.getBooksCount();
	}

	@Transactional
	@RequestMapping(value = "/Admin/Books/Name", method = RequestMethod.GET, produces = { "application/json",
	"application/xml" })
	public List<Book> readBooks(@RequestParam String searchString, @RequestParam Integer pageNo) throws SQLException {
		List<Book> book = bdao.readBooks(searchString, pageNo);
		if (book != null && book.size() > 0) {
			for (Book b : book) {
				b.setAuthors(adao.readAuthorByBook(b));
				b.setBranches(ldao.readBranchByBook(b));
				b.setGenres(gdao.readGenreByBook(b));
				b.setPublisher(pdao.readPublisherByPK(b.getPublisher().getPublisherId()));
			}
		}
		return book;
	}

	@Transactional
	@RequestMapping(value = "/Admin/Books/bookId/{id}", method = RequestMethod.GET, produces = { "application/json",
	"application/xml" })
	public Book readBookByPK(@PathVariable Integer id) throws SQLException {
		Book b = bdao.readBookByPK(id);
		if (b != null) {
			b.setAuthors(adao.readAuthorByBook(b));
			b.setBranches(ldao.readBranchByBook(b));
			b.setGenres(gdao.readGenreByBook(b));
			b.setPublisher(pdao.readPublisherByPK(b.getPublisher().getPublisherId()));
		}
		return b;
	}

	@Transactional
	@RequestMapping(value = "/Admin/Book", method = RequestMethod.DELETE, produces = { "application/json",
	"application/xml" })
	public void deleteBook(@RequestParam Integer id) throws SQLException {
		bdao.deleteBook(id);
	}

	@Transactional
	@RequestMapping(value = "/Admin/Books", method = RequestMethod.GET, produces = { "application/json",
	"application/xml" })
	public List<Book> readBooks() throws SQLException {
		List<Book> book = bdao.readAllBooks();
		if (book != null && book.size() > 0) {
			for (Book b : book) {
				b.setAuthors(adao.readAuthorByBook(b));
				b.setBranches(ldao.readBranchByBook(b));
				b.setGenres(gdao.readGenreByBook(b));
				b.setPublisher(pdao.readPublisherByPK(b.getPublisher().getPublisherId()));
			}
		}
		return book;
	}

	// @Transactional
	// @RequestMapping(value = "/Admin/Book/{searchString}", method =
	// RequestMethod.GET, produces="application/json")
	// public List<Book> readBook(@PathVariable String searchString) throws
	// SQLException {
	// List<Book> book = bdao.readBooksByTitle(searchString);
	// if(book != null && book.size() > 0) {
	// for (Book b : book) {
	// b.setAuthors(adao.readAuthorByBook(b));
	// b.setBranches(ldao.readBranchByBook(b));
	// b.setGenres(gdao.readGenreByBook(b));
	// b.setPublisher(pdao.readPublisherByPK(b.getPublisher().getPublisherId()));
	// }
	// }
	// return book;
	// }

	@Transactional
	@RequestMapping(value = "/Admin/Branch", method = RequestMethod.POST, consumes = "application/json")
	public void saveBranch(@RequestBody LibraryBranch libraryBranch) throws SQLException {
		if (libraryBranch.getBranchId() != null) {
			ldao.updateLibraryBranch(libraryBranch);
			ldao.deleteBranchBooks(libraryBranch);
		} else {
			int id = ldao.saveLibraryBranchID(libraryBranch);
			libraryBranch.setBranchId(id);
		}
		if (libraryBranch.getBooks() != null && libraryBranch.getBooks().size() > 0)
			ldao.saveBranchBooks(libraryBranch);
	}

	@Transactional
	@RequestMapping(value = "/Admin/Branches/Name", method = RequestMethod.GET, produces = { "application/json",
	"application/xml" })
	public List<LibraryBranch> readBranches(@RequestParam String searchString, @RequestParam Integer pageNo)
			throws SQLException {
		List<LibraryBranch> branch = ldao.readBranches(searchString, pageNo);
		if (branch != null && branch.size() > 0) {
			for (LibraryBranch l : branch) {
				l.setBooks(bdao.readBranchBooks(l.getBranchId()));
				l.setBookCopies(bcdao.readBookCopiesBranch(l));
				l.setBookLoans(bldao.readBookLoansBranch(l));
			}
		}
		return branch;
	}

	@Transactional
	@RequestMapping(value = "/Admin/Branches/{id}/Books", method = RequestMethod.GET, produces = { "application/json",
	"application/xml" })
	public List<Book> readBranchBooks(@PathVariable Integer id, @RequestParam Integer pageNo)
			throws SQLException {
		List<Book> book = bdao.readBooks(id, pageNo);
		if (book != null && book.size() > 0) {
			for (Book b : book) {
				b.setAuthors(adao.readAuthorByBook(b));
				b.setBranches(ldao.readBranchByBook(b));
				b.setGenres(gdao.readGenreByBook(b));
				b.setPublisher(pdao.readPublisherByPK(b.getPublisher().getPublisherId()));
			}
		}
		return book;
	}

	@Transactional
	@RequestMapping(value = "/Admin/branchesCount", method = RequestMethod.GET, produces = { "application/json",
	"application/xml" })
	public Integer getBranchCount() throws SQLException {
		return ldao.getBranchCount();
	}

	@Transactional
	@RequestMapping(value = "/Admin/Branch", method = RequestMethod.DELETE, produces = { "application/json",
	"application/xml" })
	public void deleteBranch(@RequestParam Integer id) throws SQLException {
		ldao.deleteLibraryBranch(id);
	}

	@Transactional
	@RequestMapping(value = "/Admin/Branches/branchId/{id}", method = RequestMethod.GET, produces = { "application/json",
	"application/xml" })
	public LibraryBranch readBranchByPK(@PathVariable Integer id) throws SQLException {
		LibraryBranch l = ldao.readBranchByPK(id);
		if (l != null) {
			l.setBooks(bdao.readBranchBooks(l.getBranchId()));
			l.setBookCopies(bcdao.readBookCopiesBranch(l));
			l.setBookLoans(bldao.readBookLoansBranch(l));
		}
		return l;
	}

	@Transactional
	@RequestMapping(value = "/Admin/Borrower", method = RequestMethod.POST, consumes = { "application/json",
	"application/xml" })
	public void saveBorrower(@RequestBody Borrower borrower) throws SQLException {
		if (borrower.getCardNo() != null) {
			brdao.updateBorrower(borrower);
		} else {
			brdao.saveBorrower(borrower);
		}
	}

	@Transactional
	@RequestMapping(value = "/Admin/borrowersCount", method = RequestMethod.GET, produces = { "application/json",
	"application/xml" })
	public Integer getBorrowerCount() throws SQLException {
		return brdao.getBorrowersCount();

	}

	@Transactional
	@RequestMapping(value = "/Admin/Borrowers/Name", method = RequestMethod.GET, produces = { "application/json",
	"application/xml" })
	public List<Borrower> readBorrowers(@RequestParam String searchString, @RequestParam Integer pageNo)
			throws SQLException {

		List<Borrower> borrower = brdao.readBorrowers(searchString, pageNo);

		for (Borrower b : borrower) {
			b.setBookLoans(bldao.readBookLoansBorrower(b));
		}

		return borrower;
	}

	@Transactional
	@RequestMapping(value = "/Admin/Borrower/CardNo/{id}", method = RequestMethod.GET, produces = { "application/json",
	"application/xml" })
	public Borrower readBorrowerByPK(@PathVariable Integer id) throws SQLException {
		Borrower borrower = brdao.readBorrowerByPK(id);
		if (borrower != null)
			borrower.setBookLoans(bldao.readBookLoansBorrower(borrower));
		return borrower;
	}

	@Transactional
	@RequestMapping(value = "/Admin/Borrower", method = RequestMethod.DELETE, produces = { "application/json",
	"application/xml" })
	public void deleteBorrower(@RequestParam Integer id) throws SQLException {
		brdao.deleteBorrower(id);
	}

	@Transactional
	@RequestMapping(value = "/Admin/Branches/{branchId}/Books/{bookId}/CardNo/{cardNo}/BookLoans", method = RequestMethod.DELETE, produces = { "application/json",
	"application/xml" })
	public void deleteBookLoan(@PathVariable Integer branchId, @PathVariable Integer bookId,
			@PathVariable Integer cardNo) throws SQLException {
		bldao.deleteBookLoans(branchId, bookId, cardNo);
	}

	@Transactional
	@RequestMapping(value = "/Admin/DueDate", method = RequestMethod.POST, consumes = "application/json")
	public void updateDueDate(@RequestBody BookLoans bookLoans) throws SQLException {
		if (bookLoans.getBook().getBookId() != null && bookLoans.getBorrower().getCardNo() != null
				&& bookLoans.getLibraryBranch().getBranchId() != null && bookLoans.getDateIn() == null) {
			bldao.updateDueDate(bookLoans);
		}
	}

	@Transactional
	@RequestMapping(value = "/Admin/Branches/{branchId}/Books/{bookId}/CardNo/{cardNo}/BookLoan", method = RequestMethod.GET, produces = { "application/json",
	"application/xml" })
	public BookLoans readBookLoanByPK(@PathVariable Integer branchId, @PathVariable Integer bookId,
			@PathVariable Integer cardNo) throws SQLException {
		return bldao.readBookLoanByPK(bookId, branchId, cardNo);
	}

	@Transactional
	@RequestMapping(value = "/Admin/BookLoans", method = RequestMethod.GET, produces = { "application/json",
	"application/xml" })
	public List<BookLoans> readBookLoans() throws SQLException {
		return bldao.readAllBookLoans();
	}

	@Transactional
	@RequestMapping(value = "/Admin/bookLoansCount", method = RequestMethod.GET, produces = { "application/json",
	"application/xml" })
	public Integer getBookLoansCount() throws SQLException {
		return bldao.getBookLoansCount();
	}

	@Transactional
	@RequestMapping(value = "/Admin/BookLoans/Name", method = RequestMethod.GET, produces = { "application/json",
	"application/xml" })
	public List<BookLoans> readBookLoans(@RequestParam String searchString, @RequestParam Integer pageNo)
			throws SQLException {
		return bldao.readBookLoans(searchString, pageNo);
	}

	@Transactional
	@RequestMapping(value = "/Admin/NoOfCopies", method = RequestMethod.POST, consumes = "application/json")
	public void updateNoOfCopies(@RequestBody BookCopies bookCopies) throws SQLException {
		if (bookCopies.getBook().getBookId() != null && bookCopies.getLibraryBranch().getBranchId() != null
				&& bookCopies.getNoOfCopies() == null) {
			bcdao.updateBookCopies(bookCopies, bookCopies.getNoOfCopies());
		}

	}
}
