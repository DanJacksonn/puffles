#!/usr/bin/env python

import pygame, math, sys, ConfigParser
from pygame.locals import *

class PlayerSprite(pygame.sprite.Sprite):

	def __init__(self, filename, position):		
		pygame.sprite.Sprite.__init__(self)
		# display
		self.src_image = pygame.image.load(filename)
		self.image = self.src_image
		self.rect = self.image.get_rect()
		self.rect.topleft = position
		# player movement
		self.horizontal_speed = 4.9
		self.vertical_speed = 25
		self.gravity = 3.8
		self.dx = 0 # x direction speed
		self.dy = 0 # y direction speed
		# player states
		self.dead = False
		self.reached_goal = False
		self.jumping = False
		self.rotation = 0
		# block breaking speed
		self.damage_cooldown = self.cooldown_time = 7
		
	def update(self):
		# rotate player sprite
		self.rotation -= self.dx * 2
		if self.rotation > 360 or self.rotation < -360: 
			self.rotation = 0 # restart loop
		self.image = self.rotate(self.src_image, self.rotation)
			
		# apply gravity
		if self.dy == 0: # when not falling
			self.dy = self.gravity
		else: # when falling
			# when less than max falling speed
			if self.dy < self.vertical_speed:
				# increase gravity until terminal velocity is reached
				self.dy += self.gravity
			
		# move left/right
		self.rect.x += self.dx
		# check for collisions
		collisions = pygame.sprite.spritecollide(player, level.tiles, False)
		for tile in collisions:
			if tile.name == 'goal':
				self.reached_goal = True
			else:
				self.damage_tile(tile)
				# reset position of player
				if self.rect.x < tile.rect.x:
					# moving right
					self.rect.right = tile.rect.left
				else:
					# moving left
					self.rect.left = tile.rect.right
				# bounce
				self.dx = -self.dx
				
		# bounce if out of bounds on the right
		if self.rect.x > level.width or self.rect.x < 0 : self.dx = -self.dx	
				
		# move up/down
		self.rect.y += self.dy
		# check for collisions
		collisions = pygame.sprite.spritecollide(player, level.tiles, False)
		for tile in collisions:
			if tile.name == 'goal':
				self.reached_goal = True
			else: 
				if self.rect.y > tile.rect.y:
					# moving up
					self.rect.top = tile.rect.bottom
				else:
					# moving down
					self.rect.bottom = tile.rect.top
				# stop jumping/falling
				self.dy = 0
				self.jumping = False	
		
		# kill player if out of bounds
		if self.rect.y > level.height: self.dead = True	
			
	def rotate(self, image, angle):
		# rotate the image from its centre
		old_rect = image.get_rect()
		new_image = pygame.transform.rotate(image, angle)
		new_rect = old_rect.copy()
		new_rect.center = new_image.get_rect().center
		new_image = new_image.subsurface(new_rect).copy()
		return new_image

	def damage_tile(self, tile):
		# if player isnt cooling down
		if self.damage_cooldown == self.cooldown_time:
			# if tile is breakable
			if tile.name == 'grass':
				tile.damage += 1
				if tile.damage == 3:
					# destroyed
					level.tiles.remove(tile)
					inventory.count += 1
					inventory.update_label()
				else:
					# damage tile
					tile.update_image()		
			self.damage_cooldown -= 1
 
        		
class TileSprite(pygame.sprite.Sprite):
		
	def __init__(self, name, image, position):
		pygame.sprite.Sprite.__init__(self)		
		self.name = name
		self.image = image
		self.rect = self.image.get_rect()
		self.rect.topleft = position
		self.damage = 0

	def update_image(self):
		# update tile image for new damage value
		self.image = level.tileset[self.damage + 1][0]		
	
	
class Level(object):

	def __init__(self, filename):
		self.filename = filename
		self.tiles = pygame.sprite.Group()	

	def load_level(self, level_name):		
		# load level layout
		parser = ConfigParser.ConfigParser()
		parser.read(self.filename)
		
		# load background
		self.background = parser.get(level_name, "background")
		
		# load tileset
		tileset_directory = parser.get(level_name, "tileset")
		self.tileset = self.load_tileset(tileset_directory)	

		# store level layout stored in map
		self.map = parser.get(level_name, "map").split("\n")
		
		# store key items
		key = {}
		for section in parser.sections():
			if len(section) == 1:
				key_items = dict(parser.items(section))
				key[section] = key_items
		
		# store tiles as sprites
		for level_y, line in enumerate(self.map):
			for level_x, c in enumerate(line):
				# store the name of the tile
				name = key[c]['name']
				# spawn tile
				if name == 'spawn': self.spawn = (level_x*TILE_SIZE, level_y*TILE_SIZE + 5)
				# store the coordinates of images subsurface within the tileset
				try:
					tileset_coord = key[c]['tile'].split(', ')
					tileset_coord = int(tileset_coord[0]), int(tileset_coord[1])
				except (ValueError, KeyError):
					# default to air tile
					name = 'air'
				# create sprite for current tile
				if not name == 'air': # ignore air
					tile_image = self.tileset[tileset_coord[0]][tileset_coord[1]]
					tile_sprite = TileSprite(name, tile_image, (level_x*TILE_SIZE, level_y*TILE_SIZE))
					self.tiles.add(tile_sprite)
		
		# store size of level			
		self.size = self.width, self.height = (level_x+1)*TILE_SIZE, (level_y+1)*TILE_SIZE
	
	def load_tileset(self, tileset_directory):
		# load tileset
		image = pygame.image.load(tileset_directory)
		image_width, image_height = image.get_size()
		# store every tile in the tileset
		tileset = []
		for tile_x in range(0, image_width/TILE_SIZE):
			line = []
			tileset.append(line)
			for tile_y in range(0, image_height/TILE_SIZE):
				rect = ((tile_x * TILE_SIZE), (tile_y * TILE_SIZE), TILE_SIZE, TILE_SIZE)
				# append the section of the image encompassed by the rectangle rect
				line.append(image.subsurface(rect))	
		return tileset			
			

class Inventory(object):
	position = (40, 0)
	label_position = (47, 15)
	label_color = pygame.Color("white")
		
	def __init__(self):
		# inventory contents
		self.count = 0
		# inventory display
		self.inv_tile = level.tileset[1][1]
		# label display
		self.font = pygame.font.SysFont("calibri", 16, False)
		self.label = self.font.render(str(self.count), True, self.label_color)
		# placeable block display
		self.placing_tile = level.tileset[4][0]
		self.placed_tile = level.tileset[1][0]
		self.placing_sprite = TileSprite('grass', self.placing_tile, (0, 0))
		self.placing = pygame.sprite.Group(self.placing_sprite)
				
	def update_label(self):
		# change string stored in label
		self.label = inventory.font.render(str(inventory.count), True, self.label_color)
	
class MenuSprite(pygame.sprite.Sprite):

	def __init__(self, filename, position):		
		pygame.sprite.Sprite.__init__(self)
		# menu image display
		self.image = pygame.image.load(filename)
		self.rect = self.image.get_rect()
		self.rect.topleft = position	


class Game(object):
	
	def __init__(self):
		self.clock = pygame.time.Clock()
		self.fps = 30
		# display
		self.screen = pygame.display.get_surface()
		self.background = pygame.image.load(level.background)
		# placing blocks
		self.mouse_x = self.mouse_y = 0
		self.placing = False
		# game states
		self.current_level = 0
		self.gameover = False

	def main(self):	
		# draw background to screen
		self.screen.blit(self.background, (0,0))
		
		# GAME LOOP
		while not self.gameover:
			# clock tick
			self.clock.tick(self.fps)
			
			# cooldown tick
			if player.damage_cooldown < player.cooldown_time:
				player.damage_cooldown -= 1
				if player.damage_cooldown == 0:
					player.damage_cooldown = player.cooldown_time
			
			# clear current sprites
			active_sprites.clear(self.screen, self.background)
			level.tiles.clear(self.screen, self.background)
			inventory.placing.clear(self.screen, self.background)
					
			# update active sprites
			active_sprites.update()
			
			# draw menu
			if self.current_level == 0: menu_sprite.draw(self.screen)
			
			if self.placing:			
				# draw block being placed
				inventory.placing_sprite.rect.topleft = (self.mouse_x, self.mouse_y)
				inventory.placing.draw(self.screen)	
			
			# draw tiles
			level.tiles.draw(self.screen)
			
			# draw inventory
			if inventory.count > 0:
				self.screen.blit(inventory.inv_tile, inventory.position)	
				self.screen.blit(inventory.label, inventory.label_position)
			
			# draw player
			active_sprites.draw(self.screen)
			
			# update display surface to screen
			pygame.display.flip()
			
			# USER INPUT
			for event in pygame.event.get():
				if event.type == pygame.QUIT: sys.exit(0)		
				
				# keyboard key pressed
				if event.type == pygame.KEYDOWN:
					# left
					if event.key == K_LEFT or event.key == K_a:
						player.dx = -player.horizontal_speed	
					# right
					elif event.key == K_RIGHT or event.key == K_d:
						player.dx = player.horizontal_speed		
					# up (jump)
					elif (event.key == K_UP or event.key == K_w) and not player.jumping:
						player.dy = -player.vertical_speed
						player.jumping = True	
					# down (stop rolling)
					elif (event.key == K_DOWN or event.key == K_s) and not player.jumping:
						player.dx = 0
					# e (toggle placement)
					elif event.key == K_e:
						if inventory.count > 0: self.placing = not self.placing						
					# r (reset game)
					elif event.key == K_r:
						self.start_level(self.current_level)
					# escape
					elif event.key == K_ESCAPE: sys.exit(0)
				
				# mouse move
				elif event.type == MOUSEMOTION:
					self.mouse_x, self.mouse_y = event.pos
					self.mouse_x -= self.mouse_x % 40
					self.mouse_y -= self.mouse_y % 40
				
				# mouse click
				elif event.type == MOUSEBUTTONDOWN:
					if event.button == 1:
						if inventory.count > 0 and self.mouse_y == 0:
							self.placing = not self.placing
						else:
							self.place_block()
					elif event.button == 3:
						if inventory.count > 0: self.placing = not self.placing
			
			# check the state of the player
			if player.dead:
				self.start_level(self.current_level)
				player.dead = False
			if player.reached_goal:
				# clear menu from screen
				if self.current_level == 0: menu_sprite.clear(self.screen, self.background)
				# move to next level
				self.current_level += 1
				self.start_level(self.current_level)
				player.reached_goal = False
	
	def place_block(self):
		# if no sprite is already occupying this position
		if self.placing and not self.sprite_present():
				# add block to level
				new_tile = TileSprite('grass', inventory.placed_tile, (self.mouse_x, self.mouse_y))
				level.tiles.add(new_tile)
			
				# update inventory
				inventory.count -= 1
				inventory.update_label()
				if inventory.count == 0:
					# delete inventory from screen
					self.hide_inventory()
					self.placing = False
	
	def sprite_present(self):
		# check if any sprites are occupying the tile at the current mouse position
		loaded_tiles = level.tiles.sprites()
		for tile in loaded_tiles:
			if tile.rect.topleft == (self.mouse_x, self.mouse_y):
				return True
		loaded_sprites = active_sprites.sprites()
		for sprite in loaded_sprites:
			# convert sprite position to tile position
			sprite_x, sprite_y = sprite.rect.topleft
			sprite_x -= (sprite_x % 40)
			sprite_y -= (sprite_y % 40)
			if (sprite_x, sprite_y) == (self.mouse_x, self.mouse_y):
				return True
		return False
		
	def start_level(self, level_number):
		# clear current level and load level requested
		level.tiles.empty()
		level.load_level('level' + str(self.current_level))
		# reset game progress
		player.dx = 0
		player.rotation = 0
		player.rect.topleft = level.spawn
		inventory.count = 0
		self.hide_inventory()
		
	def hide_inventory(self):
		# clear the area in which the inventory is drawn
		inv_x, inv_y = inventory.position
		bg_subsurface = self.background.subsurface(pygame.Rect(inv_x, inv_y, TILE_SIZE, TILE_SIZE))
		self.screen.blit(bg_subsurface, inventory.position)
		
if __name__ == "__main__":	
	# INITIALISATION
	pygame.init()
	
	# LOAD LEVEL
	TILE_SIZE = 40
	level = Level('level.map')
	level.load_level('level0')
	
	# LOAD GRAPHICS
	pygame.display.set_mode((level.size))
	
	# LOAD PLAYER
	player = PlayerSprite('player.png', level.spawn)
	active_sprites = pygame.sprite.Group(player)
	
	# LOAD INVENTORY
	inventory = Inventory()
	
	# LOAD MENU
	menu = PlayerSprite('menu.png', (0, 0))
	menu_sprite = pygame.sprite.Group(menu)
	
	# START GAME
	Game().main()