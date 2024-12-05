use std::env;
use std::fs::File;
use std::io::{self, BufRead};
use std::path::Path;

fn read_lines<P>(filename: P) -> io::Result<io::Lines<io::BufReader<File>>>
where P: AsRef<Path>, {
    let file = File::open(filename)?;
    Ok(io::BufReader::new(file).lines())
}

pub fn main() {
    let mut loc_a = Vec::new();
    let mut loc_b = Vec::new();

    if let Ok(file) = read_lines(env::args().nth(1).unwrap()) {
        for line in file.flatten() {
            let toks: Vec<&str> = line.split_whitespace().collect();
            loc_a.push(toks[0].parse::<i32>().unwrap());
            loc_b.push(toks[1].parse::<i32>().unwrap());
        }
    }

    loc_a.sort();
    loc_b.sort();

    let mut sum = 0;
    for i in loc_a {
        sum += i * (loc_b.iter().filter(|&j| *j == i).count() as i32);
    }

    println!("{}", sum);
}